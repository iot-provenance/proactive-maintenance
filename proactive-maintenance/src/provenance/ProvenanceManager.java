
package provenance;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.*;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
//import org.openprovenance.prov.model.WasDetectedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.Attribute;

import application.Controller;
import application.LogManager;
import dto.ActionDTO;
import dto.LogMessage;
import enums.Enums;
import enums.Enums.Entities;

/**
 * A little provenance goes a long way. ProvToolbox Tutorial 1: creating a
 * provenance document in Java and serializing it to SVG (in a file) and to
 * PROVN (on the console).
 * 
 * @author lucmoreau
 * @see <a href=
 *      "http://blog.provbook.org/2013/10/11/a-little-provenance-goes-a-long-way/">a-little-provenance-goes-a-long-way
 *      blog post</a>
 */

public class ProvenanceManager {

	public static final String PROV_NS = "provns:";
	public static final String PROVBOOK_PREFIX = "pr";

	public static final String IOT_PREFIX = "iot";
	public static final String IOT_NS = "iotns";

	private final ProvFactory pFactory;
	private final Namespace ns;

	public ArrayList<String> entityArray = new ArrayList<String>();
	public ArrayList<String> activityArray = new ArrayList<String>();
	//public ArrayList<String> attArray = new ArrayList<String>();
	public ArrayList<String> agentArray = new ArrayList<String>();
	
	private ArrayList<String> processedLogs=new ArrayList<String>();
public boolean ExtendedVersion;

	public ProvenanceManager(ProvFactory pFactory) {
		this.pFactory = pFactory;
		ns = new Namespace();
		ns.addKnownNamespaces();
		ns.register(PROVBOOK_PREFIX, PROV_NS);
		ns.register(IOT_PREFIX, IOT_NS);
		
			}

	public QualifiedName qn(String n) {
		if (n == null) {
			n = "23text";
		}
		return ns.qualifiedName(PROVBOOK_PREFIX, n, pFactory);
	}

	

	public Document makeDocumentFromLogs(ArrayList<LogMessage> logs, int dataId) {

		ArrayList<StatementOrBundle> list = new ArrayList<StatementOrBundle>();

		for (int i = 0; i < logs.size(); i++) {
			LogMessage m = logs.get(i);
			if (dataId > 0 && m.dataId > 0) {

				if (dataId != m.dataId)
					continue;
			}

			if (m.Entity == null)
				m.Entity = Enums.Entities.NONE;
			
			
			
			if(processedLogs.contains(m.Activity+m.AgentName+m.EntityName+m.Relation))
				continue;
			
			processedLogs.add(m.Activity+m.AgentName+m.EntityName+m.Relation);

			if (m.Relation == Enums.Relations.GENERATED_BY) {

				Entity e1 = generateEntity(m);
				Activity ac1 = generateActivity(m);
				WasGeneratedBy wgb = pFactory.newWasGeneratedBy(null, e1.getId(), null, ac1.getId());

				list.add(e1);
				list.add(ac1);
				list.add(wgb);
				
				if(m.Agent!=null) {
					
					Agent a1 = generateAgent(m);
		
					WasAssociatedWith waw = pFactory.newWasAssociatedWith(null, ac1.getId(), null, a1.getId());
					list.add(waw);
				}
			}

			if (m.Relation == Enums.Relations.ASSOCIATED_WITH) {

				Agent a1 = generateAgent(m);
				Activity ac1 = generateActivity(m);
				WasAssociatedWith wgb = pFactory.newWasAssociatedWith(null, ac1.getId(), null, a1.getId());

				list.add(a1);
				list.add(ac1);
				list.add(wgb);
			}
			
			if (m.Relation == Enums.Relations.ACTED_ON_BEHALF) {

				Agent a1 = pFactory.newAgent(qn(m.DelegateAgent.toString()));
				Agent a2 = generateAgent(m);
				
				ActedOnBehalfOf wgb = pFactory.newActedOnBehalfOf(null, a2.getId(), a1.getId());

				list.add(a1);
				list.add(a2);
				list.add(wgb);
			}

			if (m.Relation == Enums.Relations.ATTRIBUTED_TO) {

				Entity e1 = generateEntity(m);
				Agent a1 = generateAgent(m);
				WasAttributedTo wgb = pFactory.newWasAttributedTo(null, e1.getId(), a1.getId());

				list.add(e1);
				list.add(a1);
				list.add(wgb);
			}

			if (m.Relation == Enums.Relations.DERIVED_FROM) {

				Entity e1 = generateEntity(m);
				Entity e2 = pFactory.newEntity(qn(m.Entity2.toString()));
				WasDerivedFrom wgb = pFactory.newWasDerivedFrom(e2.getId(), e1.getId());

				list.add(e1);
				list.add(e2);
				list.add(wgb);
			}

			if (m.Relation == Enums.Relations.USED) {

				Entity e1 = generateEntity(m);
				Activity ac1 = generateActivity(m);
				Used wgb = pFactory.newUsed(ac1.getId(), e1.getId());
				list.add(e1);
				list.add(ac1);
				list.add(wgb);
			}

		}

		// WasDetectedBy
		// wdb=pFactory.newWasDetectedBy(null,quote.getId(),null,paul.getId());

		Document document = pFactory.newDocument();
		document.getStatementOrBundle().addAll(list);
		document.setNamespace(ns);
		return document;
	}

	public Agent generateAgent(LogMessage m) {
		if (!agentArray.contains(m.Agent.toString())) {
		if (m.AgentName != null) {
			Attribute e = pFactory.newAttribute(PROV_NS, "name", IOT_PREFIX, m.AgentName, null);
			m.AgentAttributes.add(e);
		
		
		 Attribute typeAttribute = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, 
                 "prov:Person", 
                 pFactory.getName().PROV_QUALIFIED_NAME);
m.AgentAttributes.add(typeAttribute);
agentArray.add(m.Agent.toString());
		}}
		return pFactory.newAgent(qn(m.Agent.toString()),m.AgentAttributes);
		
	}

	public Activity generateActivity(LogMessage m) {
		if (!activityArray.contains(m.Activity.toString())) {

			activityArray.add(m.Activity.toString());
			if (m.StartTime != null)// && !attArray.contains("ac"+m.Activity.toString()+"startTime")) 
				{
				//attArray.add("ac"+m.Activity.toString()+"startTime");
				Attribute e = pFactory.newAttribute(PROV_NS, "startTime", IOT_PREFIX, m.StartTime.toString(), null);
				m.ActivityAttributes.add(e);
			}

			if (m.EndTime != null)// && !attArray.contains("ac"+m.Activity.toString()+"endTime")) 
				{
			//	attArray.add("ac"+m.Activity.toString()+"endTime");
				Attribute e = pFactory.newAttribute(PROV_NS, "endTime", IOT_PREFIX, m.EndTime.toString(), null);
				m.ActivityAttributes.add(e);
			}
		}

		return pFactory.newActivity(qn(m.Activity.toString()), null, null, m.ActivityAttributes);
	}

	public Entity generateEntity(LogMessage m) {

		if (ExtendedVersion && m.model != null && m.Entity == Entities.CLASSIFICATION_MODEL
				&& !entityArray.contains(m.Entity.toString())) {

			entityArray.add(m.Entity.toString());
			Attribute e = pFactory.newAttribute(PROV_NS, "modelName", IOT_PREFIX, m.model.modelName, null);
			m.Attributes.add(e);

			Attribute e2 = pFactory.newAttribute(PROV_NS, "accuracy", IOT_PREFIX, m.model.Accuracy, null);
			m.Attributes.add(e2);

			Attribute e3 = pFactory.newAttribute(PROV_NS, "precision", IOT_PREFIX, m.model.Precision, null);
			m.Attributes.add(e3);

			Attribute e6 = pFactory.newAttribute(PROV_NS, "f1", IOT_PREFIX, m.model.F1Call, null);
			m.Attributes.add(e6);

			Attribute e5 = pFactory.newAttribute(PROV_NS, "recall", IOT_PREFIX, m.model.Recall, null);
			m.Attributes.add(e5);
			
			Attribute e7= pFactory.newAttribute(PROV_NS, "framework", IOT_PREFIX,
					m.model.frameworkName, null);
			m.Attributes.add(e7);


			Attribute e4 = pFactory.newAttribute(PROV_NS, "modelType", IOT_PREFIX,
					Enums.ModelTypes.SUPERVISED.toString(), null);
			m.Attributes.add(e4);
			
			addModelTrainingAttributes(m);
		}

		if (ExtendedVersion && m.model != null && m.Entity == Entities.CLUSTERING_MODEL && !entityArray.contains(m.Entity.toString())) {

			entityArray.add(m.Entity.toString());
			Attribute e = pFactory.newAttribute(PROV_NS, "modelName", IOT_PREFIX, m.model.modelName, null);
			m.Attributes.add(e);

			Attribute e2 = pFactory.newAttribute(PROV_NS, "vMeasure", IOT_PREFIX, m.model.VMeasure, null);
			m.Attributes.add(e2);

			Attribute e3 = pFactory.newAttribute(PROV_NS, "Silhouette", IOT_PREFIX, m.model.SilhouetteValue, null);
			m.Attributes.add(e3);
			Attribute e4 = pFactory.newAttribute(PROV_NS, "modelType", IOT_PREFIX,
					Enums.ModelTypes.UNSUPERVISED.toString(), null);
			m.Attributes.add(e4);
			
			Attribute e5 = pFactory.newAttribute(PROV_NS, "framework", IOT_PREFIX,
					m.model.frameworkName, null);
			m.Attributes.add(e5);
		
			
			addModelTrainingAttributes(m);
		}

		if (ExtendedVersion && m.model != null && m.Entity == Entities.RUL_MODEL && !entityArray.contains(m.Entity.toString())) {

			entityArray.add(m.Entity.toString());
			Attribute e = pFactory.newAttribute(PROV_NS, "modelName", IOT_PREFIX, m.model.modelName, null);
			m.Attributes.add(e);

			Attribute e2 = pFactory.newAttribute(PROV_NS, "MSE", IOT_PREFIX, m.model.MSE, null);
			m.Attributes.add(e2);

			Attribute e3 = pFactory.newAttribute(PROV_NS, "RMSE", IOT_PREFIX, m.model.RMSE, null);
			m.Attributes.add(e3);
			
			Attribute e7 = pFactory.newAttribute(PROV_NS, "MAE", IOT_PREFIX, m.model.MAE, null);
			m.Attributes.add(e7);

			Attribute e8 = pFactory.newAttribute(PROV_NS, "MAPE", IOT_PREFIX, m.model.MAPE, null);
			m.Attributes.add(e8);
			
			Attribute e4 = pFactory.newAttribute(PROV_NS, "modelType", IOT_PREFIX,
					Enums.ModelTypes.LSTM.toString(), null);
			m.Attributes.add(e4);
			
			Attribute e5 = pFactory.newAttribute(PROV_NS, "framework", IOT_PREFIX,
					m.model.frameworkName, null);
			m.Attributes.add(e5);
			
			addModelTrainingAttributes(m);
		}
		
		if ( ExtendedVersion &&  m.Entity == Entities.RUL_VALUE && !entityArray.contains(m.Entity.toString())) {

			entityArray.add(m.Entity.toString());
			Attribute e = pFactory.newAttribute(PROV_NS, "dataId", IOT_PREFIX, m.dataId, null);
			m.Attributes.add(e);

			Attribute e4 = pFactory.newAttribute(PROV_NS, "rulValue", IOT_PREFIX, m.rulValue,
					null);
			m.Attributes.add(e4);
		}
		if (ExtendedVersion && m.Entity == Entities.PREFAULT_VALUE && !entityArray.contains(m.Entity.toString())) {

			entityArray.add(m.Entity.toString());
			Attribute e = pFactory.newAttribute(PROV_NS, "dataId", IOT_PREFIX, m.dataId, null);
			m.Attributes.add(e);

		
		}

		if ( ExtendedVersion && m.Entity == Entities.ACTION_RECORD) {

			for (int i = 0; i < m.Actions.size(); i++) {
				ActionDTO a = m.Actions.get(i);
				Attribute e4 = pFactory.newAttribute(PROV_NS, "actionType", IOT_PREFIX, a.ActionType.toString(),
						null);
				m.Attributes.add(e4);
				Attribute e = pFactory.newAttribute(PROV_NS, "actionTarget", IOT_PREFIX, a.ActionTarget, null);
				m.Attributes.add(e);
				Attribute e2 = pFactory.newAttribute(PROV_NS, "actionTime", IOT_PREFIX, a.ActionTime.toString(),
						null);
				m.Attributes.add(e2);
				
				Attribute e5 = pFactory.newAttribute(PROV_NS, "riskAssesment", IOT_PREFIX, a.riskAssesment,
						null);
				m.Attributes.add(e5);
			}

		}
		
		if(m.EntityName!=null && m.EntityName!="") {
		//	if(!attArray.contains("en"+m.EntityName+"Name")) {
			//	attArray.add("en"+m.EntityName+"Name");
			Attribute e2 = pFactory.newAttribute(PROV_NS, "Name", IOT_PREFIX,m.EntityName,
					null);
			m.Attributes.add(e2);
			}
		//}

		return pFactory.newEntity(qn(m.Entity.toString()), m.Attributes);
	}

	public void addModelTrainingAttributes(LogMessage m) {
		Attribute e8 = pFactory.newAttribute(PROV_NS, "version", IOT_PREFIX,
				m.model.version, null);
		m.Attributes.add(e8);
		
		Attribute e10 = pFactory.newAttribute(PROV_NS, "trainingDataSize", IOT_PREFIX,
				m.model.trainingDataSize, null);
		m.Attributes.add(e10);
		
		Attribute e11 = pFactory.newAttribute(PROV_NS, "testDataSize", IOT_PREFIX,
				m.model.testDataSize, null);
		m.Attributes.add(e11);
		
		if(m.model.HyperParameters.size()>0) {
			
			Attribute e12 = pFactory.newAttribute(PROV_NS, "hyperParameters", IOT_PREFIX,
					String.join(",", m.model.HyperParameters), null);
			m.Attributes.add(e12);
		}
	
	}

	public void doConversions(Document document, String file) {

		InteropFramework intF = new InteropFramework();
		intF.writeDocument(file, document); //
		// intF.writeDocument(System.out, document, Formats.ProvFormat.PROVN);

	}

	public void closingBanner() {
		System.out.println("*************************");
	}

	public void openingBanner() {
		System.out.println("*************************");
		System.out.println("* Converting document  ");
		System.out.println("*************************");
	}

	public final static String configFile = "config.interop.properties";
	public static String configuration;
	public static String factoryClass;
	public static ProvFactory defaultFactory;

	static ProvFactory dynamicallyLoadFactory(String factory) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(factory);
			Method method = clazz.getMethod("getFactory");
			return (ProvFactory) method.invoke(new Object[0]);
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String GenerateProvenance(ArrayList<LogMessage> logs, int dataId, boolean extendedVersion) {

		if (logs == null || logs.size() == 0) {
			System.out.println("no log found to generate provenance");
			return "";
		}
		String file = ("D:\\Prove\\prov_"
				+ new java.text.SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.provn'").format(new Date()))
				.replaceAll("\\s+", "");

		String fileImage = ("D:\\Prove\\prov_"
				+ new java.text.SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()))
				.replaceAll("\\s+", "");

		Properties properties = null;// InteropFramework.getPropertiesFromClasspath(configFile);
		if (properties == null) {
			properties = new Properties();
		}
		configuration = (String) properties.get("interop.config");
		factoryClass = properties.getProperty("prov.factory");
		// defaultFactory = dynamicallyLoadFactory(factoryClass);

		defaultFactory = new org.openprovenance.prov.xml.ProvFactory();
		ProvenanceManager provManager = new ProvenanceManager(defaultFactory);
		provManager.ExtendedVersion=extendedVersion;
		provManager.openingBanner();
		Document document = provManager.makeDocumentFromLogs(logs, dataId);
		provManager.doConversions(document, file);
		provManager.closingBanner();

	//	generateGrapgh(file, fileImage+".png");
		generateGrapgh(file, fileImage+".svg",fileImage+".png");
		
return fileImage;

	}
	
	public static String GenerateProvenanceLog(ArrayList<LogMessage> logs, int dataId, boolean extendedVersion) {
		StringBuilder logString=new StringBuilder();
		if (logs == null || logs.size() == 0) {
			System.out.println("no log found to generate provenance");
			return "";
		}
		
		for (int i = 0; i < logs.size(); i++) {
			LogMessage log=logs.get(i);
			logString.append(log.getMessageSummary() +"\n");
			
		}
		
		
		
		
return logString.toString();

	}

	public static void generateGrapgh(String file, String fileImage, String filePng) {
try {
	ProvNToImageConverter.executeProvConvertCommand(file, fileImage);
	ProvNToImageConverter.convertSvgToPng(fileImage, filePng);
} catch (IOException | InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

	}

	public static void OpenFile(String file) {
		  File svgFile = new File(file);

	      // Check if Desktop is supported on the current platform
	      if (!Desktop.isDesktopSupported()) {
	          System.out.println("Desktop is not supported on this platform.");
	          return;
	      }

	      Desktop desktop = Desktop.getDesktop();

	      // Check if the file exists
	      if (svgFile.exists()) {
	          try {
	              // Open the file in the default application (typically a web browser)
	              desktop.open(svgFile);
	              System.out.println("SVG file opened in the default application.");
	          } catch (IOException e) {
	              System.out.println("An error occurred while trying to open the SVG file: " + e.getMessage());
	          }
	      } else {
	          System.out.println("The specified SVG file does not exist: " + file);
	      }
	
		
	}
}
