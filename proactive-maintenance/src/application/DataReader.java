package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dto.*;

public class DataReader {	

	private static String[] head = {"Square id","Time Interval",
			"SMS-in activity","SMS-out activity",
			"Call-in activity","Call-out activity:",
			"Internet traffic activity"};
	private String lastRow;
	
	private final String url = "jdbc:postgresql://localhost/dataset";
	private final String user = "postgres";
	private final String password = "0125527";
	private Connection connection;
	private Statement statement;
	private int dataCount = 1;
	
	
	public String[] getHead() {
		return head;
	}	

	public String getLastRow() {
		return lastRow;
	}
	
	public DataReader(int dataStart){
		dataStart=1;
		this.dataCount = dataStart+1;
		try{
			this.connection = DriverManager.getConnection(url,user,password);			
			if(connection != null) {
				System.out.println("Connected to DB");
			}else {
				System.out.println("DB connection failed");
			}
			
			this.statement = this.connection.createStatement();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public double[] readData(){
		try {


		
			String query = "select * from (\r\n"
					+ "SELECT  mid, cycle,coalesce(error, true)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid\r\n"
					+ "	FROM public.nasa where did =4) as sub\r\n"
					+ "	where sub.sid="+ this.dataCount +";";
			this.dataCount++;
			ResultSet resultSet = this.statement.executeQuery(query);
			if(resultSet.next()) {	
				boolean isError =  resultSet.getBoolean(3);
	
				//System.out.println(resultSet.getString(1));
				/*String[] row = {  resultSet.getString(4),
								resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
								resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),resultSet.getString(13),resultSet.getString(14),
								resultSet.getString(15),resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
								resultSet.getString(20),resultSet.getString(21),resultSet.getString(22),resultSet.getString(23),resultSet.getString(24),
								resultSet.getString(25),resultSet.getString(26),resultSet.getString(27)};*/
				
				String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
						resultSet.getString(20),resultSet.getString(21),resultSet.getString(23),
						resultSet.getString(1),resultSet.getString(2)};
				
				
				/*String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18)};
			*/
				
				double[] vector = new double[row.length+2];
				int i=0;
				for(String data: row) {
					if(data == null) {
						//System.out.print("YEP");
						vector[i++] = 0.0;
					}else {
						vector[i++] = Double.parseDouble(data);
					}
					//System.out.print(vector[i-1]+" ");
				}
				//vector[0] = vector[0]/1000;
				//vector[1] = vector[1];
				if(isError) {
					vector[row.length] = 1.0;
				}else {
					vector[row.length] = 0.0;
				}
				vector[row.length+1] = this.dataCount-1;
				//System.out.print("\n");				
				return vector;
			}
						
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in read!");	
		}
		return null;		
	}
	
	public ArrayList<DatasetDTO> readDataList(int did){
		try {

			ArrayList<DatasetDTO> dtoList=new ArrayList<DatasetDTO>();
		

			
			String query = 
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, sira \r\n"
					+ "	FROM public.nasa where did in (4,8) order by mid, cycle" ;
			this.dataCount++;

			ResultSet resultSet = this.statement.executeQuery(query);

			int k=0;
			ArrayList<double[]> result=new ArrayList<double[]>();
			while(resultSet.next()) {	
				
				DatasetDTO dto=new DatasetDTO();
				
			
				//System.out.println(resultSet.getString(1));
				/*String[] row = {  resultSet.getString(4),
								resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
								resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),resultSet.getString(13),resultSet.getString(14),
								resultSet.getString(15),resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
								resultSet.getString(20),resultSet.getString(21),resultSet.getString(22),resultSet.getString(23),resultSet.getString(24),
								resultSet.getString(25),resultSet.getString(26),resultSet.getString(27)};*/
				
				String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
						resultSet.getString(20),resultSet.getString(21),resultSet.getString(23),
						resultSet.getString(1),resultSet.getString(2)};
				
				
				/*String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18)};
			*/
				boolean isError =  resultSet.getBoolean(3);
			
				dto.did=did;
				dto.mid=Integer.parseInt(resultSet.getString(1));
				dto.cycle=Integer.parseInt(resultSet.getString(2));
				dto.error=isError;
				
				dto.s1=Double.parseDouble(resultSet.getString(4));
				dto.s2=Double.parseDouble(resultSet.getString(5));
				dto.s3=Double.parseDouble(resultSet.getString(6));
				dto.s4=Double.parseDouble(resultSet.getString(7));
				dto.s5=Double.parseDouble(resultSet.getString(8));
				dto.s6=Double.parseDouble(resultSet.getString(9));
				dto.s7=Double.parseDouble(resultSet.getString(10));
				dto.s8=Double.parseDouble(resultSet.getString(11));
				dto.s9=Double.parseDouble(resultSet.getString(12));
				dto.s10=Double.parseDouble(resultSet.getString(13));
				dto.s11=Double.parseDouble(resultSet.getString(14));
				dto.s12=Double.parseDouble(resultSet.getString(15));
				dto.s13=Double.parseDouble(resultSet.getString(16));
				dto.s14=Double.parseDouble(resultSet.getString(17));
				dto.s15=Double.parseDouble(resultSet.getString(18));
				dto.s16=Double.parseDouble(resultSet.getString(19));
				dto.s17=Double.parseDouble(resultSet.getString(20));
				dto.s18=Double.parseDouble(resultSet.getString(21));
				dto.s19=Double.parseDouble(resultSet.getString(22));
				dto.s20=Double.parseDouble(resultSet.getString(23));
				dto.s21=Double.parseDouble(resultSet.getString(24));
				dto.s22=Double.parseDouble(resultSet.getString(25));
				dto.s23=Double.parseDouble(resultSet.getString(26));
				dto.s24=Double.parseDouble(resultSet.getString(27));
				dto.sira=Integer.parseInt(resultSet.getString(29));
				
		
				dtoList.add(dto);
			}
			
			return dtoList;
						
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in read!");	
		}
		return null;		
	}
	
	public ArrayList<DatasetDTO> readLabeledDataListMulti(int did, String state, int limit){
		try {

			ArrayList<DatasetDTO> dtoList=new ArrayList<DatasetDTO>();
		

			
			String query = 
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, sira, CASE WHEN (SELECT count(l.dataid)\r\n"
					 + "	FROM public.label l where l.modelid in (1,5) and l.dataid=sira )>0 THEN 1 ELSE 0 END as label		FROM public.nasa\r\n"
					 + "	where did ="+did+" and state='"+state+"' order by mid, cycle limit "+limit+"" ;
			
			if(state=="") {
			 query = 
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, sira, CASE WHEN (SELECT count(l.dataid)\r\n"
					 + "	FROM public.label l where l.modelid in (1,5) and l.dataid=sira )>0 THEN 1 ELSE 0 END as label		FROM public.nasa\r\n"
					 + "	where did ="+did+" and order by mid, cycle limit "+limit+"" ;
			
			}
			 this.dataCount++;

			ResultSet resultSet = this.statement.executeQuery(query);

			int k=0;
			ArrayList<double[]> result=new ArrayList<double[]>();
			while(resultSet.next()) {	
				
				DatasetDTO dto=new DatasetDTO();
				
			
				//System.out.println(resultSet.getString(1));
				/*String[] row = {  resultSet.getString(4),
								resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
								resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),resultSet.getString(13),resultSet.getString(14),
								resultSet.getString(15),resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
								resultSet.getString(20),resultSet.getString(21),resultSet.getString(22),resultSet.getString(23),resultSet.getString(24),
								resultSet.getString(25),resultSet.getString(26),resultSet.getString(27)};*/
				
				String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
						resultSet.getString(20),resultSet.getString(21),resultSet.getString(23),
						resultSet.getString(1),resultSet.getString(2)};
				
				
				/*String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18)};
			*/
				boolean isError =  resultSet.getBoolean(3);
			
				dto.did=did;
				dto.mid=Integer.parseInt(resultSet.getString(1));
				dto.cycle=Integer.parseInt(resultSet.getString(2));
				dto.error=isError;
				
				dto.s1=Double.parseDouble(resultSet.getString(4));
				dto.s2=Double.parseDouble(resultSet.getString(5));
				dto.s3=Double.parseDouble(resultSet.getString(6));
				dto.s4=Double.parseDouble(resultSet.getString(7));
				dto.s5=Double.parseDouble(resultSet.getString(8));
				dto.s6=Double.parseDouble(resultSet.getString(9));
				dto.s7=Double.parseDouble(resultSet.getString(10));
				dto.s8=Double.parseDouble(resultSet.getString(11));
				dto.s9=Double.parseDouble(resultSet.getString(12));
				dto.s10=Double.parseDouble(resultSet.getString(13));
				dto.s11=Double.parseDouble(resultSet.getString(14));
				dto.s12=Double.parseDouble(resultSet.getString(15));
				dto.s13=Double.parseDouble(resultSet.getString(16));
				dto.s14=Double.parseDouble(resultSet.getString(17));
				dto.s15=Double.parseDouble(resultSet.getString(18));
				dto.s16=Double.parseDouble(resultSet.getString(19));
				dto.s17=Double.parseDouble(resultSet.getString(20));
				dto.s18=Double.parseDouble(resultSet.getString(21));
				dto.s19=Double.parseDouble(resultSet.getString(22));
				dto.s20=Double.parseDouble(resultSet.getString(23));
				dto.s21=Double.parseDouble(resultSet.getString(24));
				dto.s22=Double.parseDouble(resultSet.getString(25));
				dto.s23=Double.parseDouble(resultSet.getString(26));
				dto.s24=Double.parseDouble(resultSet.getString(27));
				dto.sira=Integer.parseInt(resultSet.getString(29));
				dto.label=Integer.parseInt(resultSet.getString(30));
		
				dtoList.add(dto);
			}
			
			return dtoList;
						
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in read!");	
		}
		return null;		
	}
	
	public ArrayList<DatasetDTO> readLabeledDataList(int did, int modelID, String state, int limit){
		try {

			ArrayList<DatasetDTO> dtoList=new ArrayList<DatasetDTO>();
		

			
			String query = 
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, sira, coalesce(l.label, 0) label	FROM public.nasa\r\n"
					 + "	left join public.label  l on l.dataid=sira and l.modelid="+modelID+" where did ="+did+" and state='"+state+"' order by mid, cycle limit "+limit+"" ;
			
			if(state=="") {
			 query = 
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, sira, coalesce(l.label, 0) label	FROM public.nasa\r\n"
					 + "	left join public.label  l on l.dataid=sira and l.modelid="+modelID+" where did ="+did+" and order by mid, cycle limit "+limit+"" ;
			
			}
			 this.dataCount++;

			ResultSet resultSet = this.statement.executeQuery(query);

			int k=0;
			ArrayList<double[]> result=new ArrayList<double[]>();
			while(resultSet.next()) {	
				
				DatasetDTO dto=new DatasetDTO();
				
			
				//System.out.println(resultSet.getString(1));
				/*String[] row = {  resultSet.getString(4),
								resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
								resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),resultSet.getString(13),resultSet.getString(14),
								resultSet.getString(15),resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
								resultSet.getString(20),resultSet.getString(21),resultSet.getString(22),resultSet.getString(23),resultSet.getString(24),
								resultSet.getString(25),resultSet.getString(26),resultSet.getString(27)};*/
				
				String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
						resultSet.getString(20),resultSet.getString(21),resultSet.getString(23),
						resultSet.getString(1),resultSet.getString(2)};
				
				
				/*String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18)};
			*/
				boolean isError =  resultSet.getBoolean(3);
			
				dto.did=did;
				dto.mid=Integer.parseInt(resultSet.getString(1));
				dto.cycle=Integer.parseInt(resultSet.getString(2));
				dto.error=isError;
				
				dto.s1=Double.parseDouble(resultSet.getString(4));
				dto.s2=Double.parseDouble(resultSet.getString(5));
				dto.s3=Double.parseDouble(resultSet.getString(6));
				dto.s4=Double.parseDouble(resultSet.getString(7));
				dto.s5=Double.parseDouble(resultSet.getString(8));
				dto.s6=Double.parseDouble(resultSet.getString(9));
				dto.s7=Double.parseDouble(resultSet.getString(10));
				dto.s8=Double.parseDouble(resultSet.getString(11));
				dto.s9=Double.parseDouble(resultSet.getString(12));
				dto.s10=Double.parseDouble(resultSet.getString(13));
				dto.s11=Double.parseDouble(resultSet.getString(14));
				dto.s12=Double.parseDouble(resultSet.getString(15));
				dto.s13=Double.parseDouble(resultSet.getString(16));
				dto.s14=Double.parseDouble(resultSet.getString(17));
				dto.s15=Double.parseDouble(resultSet.getString(18));
				dto.s16=Double.parseDouble(resultSet.getString(19));
				dto.s17=Double.parseDouble(resultSet.getString(20));
				dto.s18=Double.parseDouble(resultSet.getString(21));
				dto.s19=Double.parseDouble(resultSet.getString(22));
				dto.s20=Double.parseDouble(resultSet.getString(23));
				dto.s21=Double.parseDouble(resultSet.getString(24));
				dto.s22=Double.parseDouble(resultSet.getString(25));
				dto.s23=Double.parseDouble(resultSet.getString(26));
				dto.s24=Double.parseDouble(resultSet.getString(27));
				dto.sira=Integer.parseInt(resultSet.getString(29));
				dto.label=Integer.parseInt(resultSet.getString(30));
		
				dtoList.add(dto);
			}
			
			return dtoList;
						
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in read!");	
		}
		return null;		
	}
	
	public void insertLabelData(int modelId, List<Integer> dataId) {
		
		dataId.forEach((n)->insertLabelData(modelId,n));
		LogManager.log("Data Labeled Model Id:"+modelId +" data count: "+dataId.size());
	}
	
	public void insertLabelData(int modelId, int dataId) {
		
		String query="INSERT INTO public.label(\r\n"
				+ " dataid, modelid, label, labeltime)\r\n"
				+ "	VALUES ( "+dataId+", "+modelId+", 1, NOW());";
		
		try {
			this.statement.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void insertAnomaly(int dataId, int modelId, double label) {
		String query="INSERT INTO public.anomaly(\r\n"
				+ " dataid, modelid, label, labeltime)\r\n"
				+ "	VALUES ( "+dataId+", "+modelId+", 1, NOW()); update public.nasa set state='anomaly' where sira="+dataId;
		
		try {
			this.statement.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ArrayList<double[]> readData(int max){
		try {


		
			String query = "select * from (\r\n"
					+ "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid\r\n"
					+ "	FROM public.nasa where did =4 ) as sub\r\n"
					+ "	where sub.sid<="+ max +";";
			this.dataCount++;

			ResultSet resultSet = this.statement.executeQuery(query);

			int k=0;
			ArrayList<double[]> result=new ArrayList<double[]>();
			while(resultSet.next() && k<max) {	
				k++;
				boolean isError =  resultSet.getBoolean(3);
	
				//System.out.println(resultSet.getString(1));
				/*String[] row = {  resultSet.getString(4),
								resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
								resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),resultSet.getString(13),resultSet.getString(14),
								resultSet.getString(15),resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
								resultSet.getString(20),resultSet.getString(21),resultSet.getString(22),resultSet.getString(23),resultSet.getString(24),
								resultSet.getString(25),resultSet.getString(26),resultSet.getString(27)};*/
				
				String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
						resultSet.getString(20),resultSet.getString(21),resultSet.getString(23),
						resultSet.getString(1),resultSet.getString(2)};
				
				
				/*String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18)};
			*/
				
				double[] vector = new double[row.length+2];
				int i = 0;
				for(String data: row) {
					if(data == null) {
						//System.out.print("YEP");
						vector[i++] = 0.0;
					}else {
						vector[i++] = Double.parseDouble(data);
					}
					//System.out.print(vector[i-1]+" ");
				}
				//vector[0] = vector[0]/1000;
				//vector[1] = vector[1];
				if(isError) {
					vector[row.length] = 1.0;
				}else {
					vector[row.length] = 0.0;
				}
				vector[row.length+1] = this.dataCount-1;
				//System.out.print("\n");				
				result.add(vector);
			}
			
			return result;
						
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in read!");	
		}
		return null;		
	}

	
	
	public double[] readDataSecom(){
		try {
			this.dataCount=1;
		
			String query = "SELECT id, \"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\", \"10\", \"11\", \"12\", \"13\", \"14\", \"15\", \"16\", \"17\", \"18\", \"19\", \"20\", \"21\", \"22\", \"23\", \"24\", \"25\", \"26\", \"27\", \"28\", \"29\", \"30\", \"31\", \"32\", \"33\", \"34\", \"35\", \"36\", \"37\", \"38\", \"39\", \"40\", \"41\", \"42\", \"43\", \"44\", \"45\", \"46\", \"47\", \"48\", \"49\", \"50\", \"51\", \"52\", \"53\", \"54\", \"55\", \"56\", \"57\", \"58\", \"59\", \"60\", \"61\", \"62\", \"63\", \"64\", \"65\", \"66\", \"67\", \"68\", \"69\", \"70\", \"71\", \"72\", \"73\", \"74\", \"75\", \"76\", \"77\", \"78\", \"79\", \"80\", \"81\", \"82\", \"83\", \"84\", \"85\", \"86\", \"87\", \"88\", \"89\", \"90\", \"91\", \"92\", \"93\", \"94\", \"95\", \"96\", \"97\", \"98\", \"99\", \"100\", \"101\", \"102\", \"103\", \"104\", \"105\", \"106\", \"107\", \"108\", \"109\", \"110\", \"111\", \"112\", \"113\", \"114\", \"115\", \"116\", \"117\", \"118\", \"119\", \"120\", \"121\", \"122\", \"123\", \"124\", \"125\", \"126\", \"127\", \"128\", \"129\", \"130\", \"131\", \"132\", \"133\", \"134\", \"135\", \"136\", \"137\", \"138\", \"139\", \"140\", \"141\", \"142\", \"143\", \"144\", \"145\", \"146\", \"147\", \"148\", \"149\", \"150\", \"151\", \"152\", \"153\", \"154\", \"155\", \"156\", \"157\", \"158\", \"159\", \"160\", \"161\", \"162\", \"163\", \"164\", \"165\", \"166\", \"167\", \"168\", \"169\", \"170\", \"171\", \"172\", \"173\", \"174\", \"175\", \"176\", \"177\", \"178\", \"179\", \"180\", \"181\", \"182\", \"183\", \"184\", \"185\", \"186\", \"187\", \"188\", \"189\", \"190\", \"191\", \"192\", \"193\", \"194\", \"195\", \"196\", \"197\", \"198\", \"199\", \"200\", \"201\", \"202\", \"203\", \"204\", \"205\", \"206\", \"207\", \"208\", \"209\", \"210\", \"211\", \"212\", \"213\", \"214\", \"215\", \"216\", \"217\", \"218\", \"219\", \"220\", \"221\", \"222\", \"223\", \"224\", \"225\", \"226\", \"227\", \"228\", \"229\", \"230\", \"231\", \"232\", \"233\", \"234\", \"235\", \"236\", \"237\", \"238\", \"239\", \"240\", \"241\", \"242\", \"243\", \"244\", \"245\", \"246\", \"247\", \"248\", \"249\", \"250\", \"251\", \"252\", \"253\", \"254\", \"255\", \"256\", \"257\", \"258\", \"259\", \"260\", \"261\", \"262\", \"263\", \"264\", \"265\", \"266\", \"267\", \"268\", \"269\", \"270\", \"271\", \"272\", \"273\", \"274\", \"275\", \"276\", \"277\", \"278\", \"279\", \"280\", \"281\", \"282\", \"283\", \"284\", \"285\", \"286\", \"287\", \"288\", \"289\", \"290\", \"291\", \"292\", \"293\", \"294\", \"295\", \"296\", \"297\", \"298\", \"299\", \"300\", \"301\", \"302\", \"303\", \"304\", \"305\", \"306\", \"307\", \"308\", \"309\", \"310\", \"311\", \"312\", \"313\", \"314\", \"315\", \"316\", \"317\", \"318\", \"319\", \"320\", \"321\", \"322\", \"323\", \"324\", \"325\", \"326\", \"327\", \"328\", \"329\", \"330\", \"331\", \"332\", \"333\", \"334\", \"335\", \"336\", \"337\", \"338\", \"339\", \"340\", \"341\", \"342\", \"343\", \"344\", \"345\", \"346\", \"347\", \"348\", \"349\", \"350\", \"351\", \"352\", \"353\", \"354\", \"355\", \"356\", \"357\", \"358\", \"359\", \"360\", \"361\", \"362\", \"363\", \"364\", \"365\", \"366\", \"367\", \"368\", \"369\", \"370\", \"371\", \"372\", \"373\", \"374\", \"375\", \"376\", \"377\", \"378\", \"379\", \"380\", \"381\", \"382\", \"383\", \"384\", \"385\", \"386\", \"387\", \"388\", \"389\", \"390\", \"391\", \"392\", \"393\", \"394\", \"395\", \"396\", \"397\", \"398\", \"399\", \"400\", \"401\", \"402\", \"403\", \"404\", \"405\", \"406\", \"407\", \"408\", \"409\", \"410\", \"411\", \"412\", \"413\", \"414\", \"415\", \"416\", \"417\", \"418\", \"419\", \"420\", \"421\", \"422\", \"423\", \"424\", \"425\", \"426\", \"427\", \"428\", \"429\", \"430\", \"431\", \"432\", \"433\", \"434\", \"435\", \"436\", \"437\", \"438\", \"439\", \"440\", \"441\", \"442\", \"443\", \"444\", \"445\", \"446\", \"447\", \"448\", \"449\", \"450\", \"451\", \"452\", \"453\", \"454\", \"455\", \"456\", \"457\", \"458\", \"459\", \"460\", \"461\", \"462\", \"463\", \"464\", \"465\", \"466\", \"467\", \"468\", \"469\", \"470\", \"471\", \"472\", \"473\", \"474\", \"475\", \"476\", \"477\", \"478\", \"479\", \"480\", \"481\", \"482\", \"483\", \"484\", \"485\", \"486\", \"487\", \"488\", \"489\", \"490\", \"491\", \"492\", \"493\", \"494\", \"495\", \"496\", \"497\", \"498\", \"499\", \"500\", \"501\", \"502\", \"503\", \"504\", \"505\", \"506\", \"507\", \"508\", \"509\", \"510\", \"511\", \"512\", \"513\", \"514\", \"515\", \"516\", \"517\", \"518\", \"519\", \"520\", \"521\", \"522\", \"523\", \"524\", \"525\", \"526\", \"527\", \"528\", \"529\", \"530\", \"531\", \"532\", \"533\", \"534\", \"535\", \"536\", \"537\", \"538\", \"539\", \"540\", \"541\", \"542\", \"543\", \"544\", \"545\", \"546\", \"547\", \"548\", \"549\", \"550\", \"551\", \"552\", \"553\", \"554\", \"555\", \"556\", \"557\", \"558\", \"559\", \"560\", \"561\", \"562\", \"563\", \"564\", \"565\", \"566\", \"567\", \"568\", \"569\", \"570\", \"571\", \"572\", \"573\", \"574\", \"575\", \"576\", \"577\", \"578\", \"579\", \"580\", \"581\", \"582\", \"583\", \"584\", \"585\", \"586\", \"587\", \"588\", \"589\", \"590\", \"591\", error\r\n"
					+ "	\r\n"
					+ "	FROM public.secom where id ="+ this.dataCount +";";
			this.dataCount++;
			ResultSet resultSet = this.statement.executeQuery(query);
			if(resultSet.next()) {				
				//System.out.println(resultSet.getString(1));
				String[] row = { resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),
								resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
								resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),resultSet.getString(13),resultSet.getString(14),
								resultSet.getString(15),resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
								resultSet.getString(20),resultSet.getString(21),resultSet.getString(22),resultSet.getString(23),resultSet.getString(24),
								resultSet.getString(25),resultSet.getString(26),resultSet.getString(27),resultSet.getString(27)
								,resultSet.getString(28)
								,resultSet.getString(29)
								,resultSet.getString(30)
								,resultSet.getString(31)
								,resultSet.getString(32)
								,resultSet.getString(33)
								,resultSet.getString(34)
								,resultSet.getString(35)
								,resultSet.getString(36)
								,resultSet.getString(37)
								,resultSet.getString(38)
								,resultSet.getString(39)
								,resultSet.getString(40)
								,resultSet.getString(41)
								,resultSet.getString(42)
								,resultSet.getString(43)
								,resultSet.getString(44)
								,resultSet.getString(45)
								,resultSet.getString(46)
								,resultSet.getString(47)
								,resultSet.getString(48)
								,resultSet.getString(49)
								,resultSet.getString(50)
								,resultSet.getString(51)
								,resultSet.getString(52)
								,resultSet.getString(53)
								,resultSet.getString(54)
								,resultSet.getString(55)
								,resultSet.getString(56)
								,resultSet.getString(57)
								,resultSet.getString(58)
								,resultSet.getString(59)
								,resultSet.getString(60)
								,resultSet.getString(61)
								,resultSet.getString(62)
								,resultSet.getString(63)
								,resultSet.getString(64)
								,resultSet.getString(65)
								,resultSet.getString(66)
								,resultSet.getString(67)
								,resultSet.getString(68)
								,resultSet.getString(69)
								,resultSet.getString(70)
								,resultSet.getString(71)
								,resultSet.getString(72)
								,resultSet.getString(73)
								,resultSet.getString(74)
								,resultSet.getString(75)
								,resultSet.getString(76)
								,resultSet.getString(77)
								,resultSet.getString(78)
								,resultSet.getString(79)
								,resultSet.getString(80)
								,resultSet.getString(81)
								,resultSet.getString(82)
								,resultSet.getString(83)
								,resultSet.getString(84)
								,resultSet.getString(85)
								,resultSet.getString(86)
								,resultSet.getString(87)
								,resultSet.getString(88)
								,resultSet.getString(89)
								,resultSet.getString(90)
								,resultSet.getString(91)
								,resultSet.getString(92)
								,resultSet.getString(93)
								,resultSet.getString(94)
								,resultSet.getString(95)
								,resultSet.getString(96)
								,resultSet.getString(97)
								,resultSet.getString(98)
								,resultSet.getString(99)
								,resultSet.getString(100)
								,resultSet.getString(101)
								,resultSet.getString(102)
								,resultSet.getString(103)
								,resultSet.getString(104)
								,resultSet.getString(105)
								,resultSet.getString(106)
								,resultSet.getString(107)
								,resultSet.getString(108)
								,resultSet.getString(109)
								,resultSet.getString(110)
								,resultSet.getString(111)
								,resultSet.getString(112)
								,resultSet.getString(113)
								,resultSet.getString(114)
								,resultSet.getString(115)
								,resultSet.getString(116)
								,resultSet.getString(117)
								,resultSet.getString(118)
								,resultSet.getString(119)
								,resultSet.getString(120)
								,resultSet.getString(121)
								,resultSet.getString(122)
								,resultSet.getString(123)
								,resultSet.getString(124)
								,resultSet.getString(125)
								,resultSet.getString(126)
								,resultSet.getString(127)
								,resultSet.getString(128)
								,resultSet.getString(129)
								,resultSet.getString(130)
								,resultSet.getString(131)
								,resultSet.getString(132)
								,resultSet.getString(133)
								,resultSet.getString(134)
								,resultSet.getString(135)
								,resultSet.getString(136)
								,resultSet.getString(137)
								,resultSet.getString(138)
								,resultSet.getString(139)
								,resultSet.getString(140)
								,resultSet.getString(141)
								,resultSet.getString(142)
								,resultSet.getString(143)
								,resultSet.getString(144)
								,resultSet.getString(145)
								,resultSet.getString(146)
								,resultSet.getString(147)
								,resultSet.getString(148)
								,resultSet.getString(149)
								,resultSet.getString(150)
								,resultSet.getString(151)
								,resultSet.getString(152)
								,resultSet.getString(153)
								,resultSet.getString(154)
								,resultSet.getString(155)
								,resultSet.getString(156)
								,resultSet.getString(157)
								,resultSet.getString(158)
								,resultSet.getString(159)
								,resultSet.getString(160)
								,resultSet.getString(161)
								,resultSet.getString(162)
								,resultSet.getString(163)
								,resultSet.getString(164)
								,resultSet.getString(165)
								,resultSet.getString(166)
								,resultSet.getString(167)
								,resultSet.getString(168)
								,resultSet.getString(169)
								,resultSet.getString(170)
								,resultSet.getString(171)
								,resultSet.getString(172)
								,resultSet.getString(173)
								,resultSet.getString(174)
								,resultSet.getString(175)
								,resultSet.getString(176)
								,resultSet.getString(177)
								,resultSet.getString(178)
								,resultSet.getString(179)
								,resultSet.getString(180)
								,resultSet.getString(181)
								,resultSet.getString(182)
								,resultSet.getString(183)
								,resultSet.getString(184)
								,resultSet.getString(185)
								,resultSet.getString(186)
								,resultSet.getString(187)
								,resultSet.getString(188)
								,resultSet.getString(189)
								,resultSet.getString(190)
								,resultSet.getString(191)
								,resultSet.getString(192)
								,resultSet.getString(193)
								,resultSet.getString(194)
								,resultSet.getString(195)
								,resultSet.getString(196)
								,resultSet.getString(197)
								,resultSet.getString(198)
								,resultSet.getString(199)
								,resultSet.getString(200)
								,resultSet.getString(201)
								,resultSet.getString(202)
								,resultSet.getString(203)
								,resultSet.getString(204)
								,resultSet.getString(205)
								,resultSet.getString(206)
								,resultSet.getString(207)
								,resultSet.getString(208)
								,resultSet.getString(209)
								,resultSet.getString(210)
								,resultSet.getString(211)
								,resultSet.getString(212)
								,resultSet.getString(213)
								,resultSet.getString(214)
								,resultSet.getString(215)
								,resultSet.getString(216)
								,resultSet.getString(217)
								,resultSet.getString(218)
								,resultSet.getString(219)
								,resultSet.getString(220)
								,resultSet.getString(221)
								,resultSet.getString(222)
								,resultSet.getString(223)
								,resultSet.getString(224)
								,resultSet.getString(225)
								,resultSet.getString(226)
								,resultSet.getString(227)
								,resultSet.getString(228)
								,resultSet.getString(229)
								,resultSet.getString(230)
								,resultSet.getString(231)
								,resultSet.getString(232)
								,resultSet.getString(233)
								,resultSet.getString(234)
								,resultSet.getString(235)
								,resultSet.getString(236)
								,resultSet.getString(237)
								,resultSet.getString(238)
								,resultSet.getString(239)
								,resultSet.getString(240)
								,resultSet.getString(241)
								,resultSet.getString(242)
								,resultSet.getString(243)
								,resultSet.getString(244)
								,resultSet.getString(245)
								,resultSet.getString(246)
								,resultSet.getString(247)
								,resultSet.getString(248)
								,resultSet.getString(249)
								,resultSet.getString(250)
								,resultSet.getString(251)
								,resultSet.getString(252)
								,resultSet.getString(253)
								,resultSet.getString(254)
								,resultSet.getString(255)
								,resultSet.getString(256)
								,resultSet.getString(257)
								,resultSet.getString(258)
								,resultSet.getString(259)
								,resultSet.getString(260)
								,resultSet.getString(261)
								,resultSet.getString(262)
								,resultSet.getString(263)
								,resultSet.getString(264)
								,resultSet.getString(265)
								,resultSet.getString(266)
								,resultSet.getString(267)
								,resultSet.getString(268)
								,resultSet.getString(269)
								,resultSet.getString(270)
								,resultSet.getString(271)
								,resultSet.getString(272)
								,resultSet.getString(273)
								,resultSet.getString(274)
								,resultSet.getString(275)
								,resultSet.getString(276)
								,resultSet.getString(277)
								,resultSet.getString(278)
								,resultSet.getString(279)
								,resultSet.getString(280)
								,resultSet.getString(281)
								,resultSet.getString(282)
								,resultSet.getString(283)
								,resultSet.getString(284)
								,resultSet.getString(285)
								,resultSet.getString(286)
								,resultSet.getString(287)
								,resultSet.getString(288)
								,resultSet.getString(289)
								,resultSet.getString(290)
								,resultSet.getString(291)
								,resultSet.getString(292)
								,resultSet.getString(293)
								,resultSet.getString(294)
								,resultSet.getString(295)
								,resultSet.getString(296)
								,resultSet.getString(297)
								,resultSet.getString(298)
								,resultSet.getString(299)
								,resultSet.getString(300)
								,resultSet.getString(301)
								,resultSet.getString(302)
								,resultSet.getString(303)
								,resultSet.getString(304)
								,resultSet.getString(305)
								,resultSet.getString(306)
								,resultSet.getString(307)
								,resultSet.getString(308)
								,resultSet.getString(309)
								,resultSet.getString(310)
								,resultSet.getString(311)
								,resultSet.getString(312)
								,resultSet.getString(313)
								,resultSet.getString(314)
								,resultSet.getString(315)
								,resultSet.getString(316)
								,resultSet.getString(317)
								,resultSet.getString(318)
								,resultSet.getString(319)
								,resultSet.getString(320)
								,resultSet.getString(321)
								,resultSet.getString(322)
								,resultSet.getString(323)
								,resultSet.getString(324)
								,resultSet.getString(325)
								,resultSet.getString(326)
								,resultSet.getString(327)
								,resultSet.getString(328)
								,resultSet.getString(329)
								,resultSet.getString(330)
								,resultSet.getString(331)
								,resultSet.getString(332)
								,resultSet.getString(333)
								,resultSet.getString(334)
								,resultSet.getString(335)
								,resultSet.getString(336)
								,resultSet.getString(337)
								,resultSet.getString(338)
								,resultSet.getString(339)
								,resultSet.getString(340)
								,resultSet.getString(341)
								,resultSet.getString(342)
								,resultSet.getString(343)
								,resultSet.getString(344)
								,resultSet.getString(345)
								,resultSet.getString(346)
								,resultSet.getString(347)
								,resultSet.getString(348)
								,resultSet.getString(349)
								,resultSet.getString(350)
								,resultSet.getString(351)
								,resultSet.getString(352)
								,resultSet.getString(353)
								,resultSet.getString(354)
								,resultSet.getString(355)
								,resultSet.getString(356)
								,resultSet.getString(357)
								,resultSet.getString(358)
								,resultSet.getString(359)
								,resultSet.getString(360)
								,resultSet.getString(361)
								,resultSet.getString(362)
								,resultSet.getString(363)
								,resultSet.getString(364)
								,resultSet.getString(365)
								,resultSet.getString(366)
								,resultSet.getString(367)
								,resultSet.getString(368)
								,resultSet.getString(369)
								,resultSet.getString(370)
								,resultSet.getString(371)
								,resultSet.getString(372)
								,resultSet.getString(373)
								,resultSet.getString(374)
								,resultSet.getString(375)
								,resultSet.getString(376)
								,resultSet.getString(377)
								,resultSet.getString(378)
								,resultSet.getString(379)
								,resultSet.getString(380)
								,resultSet.getString(381)
								,resultSet.getString(382)
								,resultSet.getString(383)
								,resultSet.getString(384)
								,resultSet.getString(385)
								,resultSet.getString(386)
								,resultSet.getString(387)
								,resultSet.getString(388)
								,resultSet.getString(389)
								,resultSet.getString(390)
								,resultSet.getString(391)
								,resultSet.getString(392)
								,resultSet.getString(393)
								,resultSet.getString(394)
								,resultSet.getString(395)
								,resultSet.getString(396)
								,resultSet.getString(397)
								,resultSet.getString(398)
								,resultSet.getString(399)
								,resultSet.getString(400)
								,resultSet.getString(401)
								,resultSet.getString(402)
								,resultSet.getString(403)
								,resultSet.getString(404)
								,resultSet.getString(405)
								,resultSet.getString(406)
								,resultSet.getString(407)
								,resultSet.getString(408)
								,resultSet.getString(409)
								,resultSet.getString(410)
								,resultSet.getString(411)
								,resultSet.getString(412)
								,resultSet.getString(413)
								,resultSet.getString(414)
								,resultSet.getString(415)
								,resultSet.getString(416)
								,resultSet.getString(417)
								,resultSet.getString(418)
								,resultSet.getString(419)
								,resultSet.getString(420)
								,resultSet.getString(421)
								,resultSet.getString(422)
								,resultSet.getString(423)
								,resultSet.getString(424)
								,resultSet.getString(425)
								,resultSet.getString(426)
								,resultSet.getString(427)
								,resultSet.getString(428)
								,resultSet.getString(429)
								,resultSet.getString(430)
								,resultSet.getString(431)
								,resultSet.getString(432)
								,resultSet.getString(433)
								,resultSet.getString(434)
								,resultSet.getString(435)
								,resultSet.getString(436)
								,resultSet.getString(437)
								,resultSet.getString(438)
								,resultSet.getString(439)
								,resultSet.getString(440)
								,resultSet.getString(441)
								,resultSet.getString(442)
								,resultSet.getString(443)
								,resultSet.getString(444)
								,resultSet.getString(445)
								,resultSet.getString(446)
								,resultSet.getString(447)
								,resultSet.getString(448)
								,resultSet.getString(449)
								,resultSet.getString(450)
								,resultSet.getString(451)
								,resultSet.getString(452)
								,resultSet.getString(453)
								,resultSet.getString(454)
								,resultSet.getString(455)
								,resultSet.getString(456)
								,resultSet.getString(457)
								,resultSet.getString(458)
								,resultSet.getString(459)
								,resultSet.getString(460)
								,resultSet.getString(461)
								,resultSet.getString(462)
								,resultSet.getString(463)
								,resultSet.getString(464)
								,resultSet.getString(465)
								,resultSet.getString(466)
								,resultSet.getString(467)
								,resultSet.getString(468)
								,resultSet.getString(469)
								,resultSet.getString(470)
								,resultSet.getString(471)
								,resultSet.getString(472)
								,resultSet.getString(473)
								,resultSet.getString(474)
								,resultSet.getString(475)
								,resultSet.getString(476)
								,resultSet.getString(477)
								,resultSet.getString(478)
								,resultSet.getString(479)
								,resultSet.getString(480)
								,resultSet.getString(481)
								,resultSet.getString(482)
								,resultSet.getString(483)
								,resultSet.getString(484)
								,resultSet.getString(485)
								,resultSet.getString(486)
								,resultSet.getString(487)
								,resultSet.getString(488)
								,resultSet.getString(489)
								,resultSet.getString(490)
								,resultSet.getString(491)
								,resultSet.getString(492)
								,resultSet.getString(493)
								,resultSet.getString(494)
								,resultSet.getString(495)
								,resultSet.getString(496)
								,resultSet.getString(497)
								,resultSet.getString(498)
								,resultSet.getString(499)
								,resultSet.getString(500)
								,resultSet.getString(501)
								,resultSet.getString(502)
								,resultSet.getString(503)
								,resultSet.getString(504)
								,resultSet.getString(505)
								,resultSet.getString(506)
								,resultSet.getString(507)
								,resultSet.getString(508)
								,resultSet.getString(509)
								,resultSet.getString(510)
								,resultSet.getString(511)
								,resultSet.getString(512)
								,resultSet.getString(513)
								,resultSet.getString(514)
								,resultSet.getString(515)
								,resultSet.getString(516)
								,resultSet.getString(517)
								,resultSet.getString(518)
								,resultSet.getString(519)
								,resultSet.getString(520)
								,resultSet.getString(521)
								,resultSet.getString(522)
								,resultSet.getString(523)
								,resultSet.getString(524)
								,resultSet.getString(525)
								,resultSet.getString(526)
								,resultSet.getString(527)
								,resultSet.getString(528)
								,resultSet.getString(529)
								,resultSet.getString(530)
								,resultSet.getString(531)
								,resultSet.getString(532)
								,resultSet.getString(533)
								,resultSet.getString(534)
								,resultSet.getString(535)
								,resultSet.getString(536)
								,resultSet.getString(537)
								,resultSet.getString(538)
								,resultSet.getString(539)
								,resultSet.getString(540)
								,resultSet.getString(541)
								,resultSet.getString(542)
								,resultSet.getString(543)
								,resultSet.getString(544)
								,resultSet.getString(545)
								,resultSet.getString(546)
								,resultSet.getString(547)
								,resultSet.getString(548)
								,resultSet.getString(549)
								,resultSet.getString(550)
								,resultSet.getString(551)
								,resultSet.getString(552)
								,resultSet.getString(553)
								,resultSet.getString(554)
								,resultSet.getString(555)
								,resultSet.getString(556)
								,resultSet.getString(557)
								,resultSet.getString(558)
								,resultSet.getString(559)
								,resultSet.getString(560)
								,resultSet.getString(561)
								,resultSet.getString(562)
								,resultSet.getString(563)
								,resultSet.getString(564)
								,resultSet.getString(565)
								,resultSet.getString(566)
								,resultSet.getString(567)
								,resultSet.getString(568)
								,resultSet.getString(569)
								,resultSet.getString(570)
								,resultSet.getString(571)
								,resultSet.getString(572)
								,resultSet.getString(573)
								,resultSet.getString(574)
								,resultSet.getString(575)
								,resultSet.getString(576)
								,resultSet.getString(577)
								,resultSet.getString(578)
								,resultSet.getString(579)
								,resultSet.getString(580)
								,resultSet.getString(581)
								,resultSet.getString(582)
								,resultSet.getString(583)
								,resultSet.getString(584)
								,resultSet.getString(585)
								,resultSet.getString(586)
								,resultSet.getString(587)
								,resultSet.getString(588)
								,resultSet.getString(589)
								,resultSet.getString(590)
								,resultSet.getString(591)
								,resultSet.getString(592)
};
				int error=resultSet.getInt(593);
				boolean isError = error ==1;
				
				double[] vector = new double[row.length+2];
				int i=0;
				for(String data: row) {
					if(data == null) {
						//System.out.print("YEP");
						vector[i++] = 0.0;
					}else {
						vector[i++] = Double.parseDouble(data);
					}
					//System.out.print(vector[i-1]+" ");
				}
				//vector[0] = vector[0]/1000;
				//vector[1] = vector[1];
				if(isError) {
					vector[row.length] = 1.0;
				}else {
					vector[row.length] = 0.0;
				}
				vector[row.length+1] = this.dataCount-1;
				//System.out.print("\n");				
				return vector;
			}
						
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in read!");	
		}
		return null;		
	}

	
	public void closeReader(){
		
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in close!");	
		}
	}
	
	public String printVector(double[] vector) {
		String tmpStr = "" + vector[0];
		
		for(int i=1;i<vector.length;i++) {
			tmpStr = tmpStr +  ", " + vector[i];
		}		
		return tmpStr;		
	}

	public ArrayList<AnomalyDTO>  getAnomaly(int dataId) {
		// TODO Auto-generated method stub
		try {

			ArrayList<AnomalyDTO> dtoList=new ArrayList<AnomalyDTO>();
		

			
			String query ="SELECT id,dataid,modelid,label,labeltime,rul,rultime from public.anomaly where dataid ="+dataId ;
		

			ResultSet resultSet = this.statement.executeQuery(query);

			while(resultSet.next()) {	
				
				AnomalyDTO dto=new AnomalyDTO();
				
			
					
			
	
				dto.id=Integer.parseInt(resultSet.getString("id"));
				dto.dataId=Integer.parseInt(resultSet.getString("dataid"));
				dto.modelId=Integer.parseInt(resultSet.getString("modelid"));
				dto.label=Integer.parseInt(resultSet.getString("label"));
				dto.labelDate=resultSet.getString("labeltime");
				dto.rul=Double.parseDouble(resultSet.getString("rul"));
				dto.rulDate=resultSet.getString("rultime");
		
				dtoList.add(dto);
			}
			
			return dtoList;
						
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in read!");	
		}
		return null;		
	}

	public ArrayList<AnomalyDTO>  getAnomaliesRUL() {
		// TODO Auto-generated method stub
		try {

			ArrayList<AnomalyDTO> dtoList=new ArrayList<AnomalyDTO>();
		

			
			String query ="SELECT a.id,a.dataid,a.modelid,a.label,a.labeltime,a.rul,a.rultime from public.anomaly  a inner join public.nasa n on a.dataid=n.sira  where n.state='rul' and rultime> '"+ Instant.now().minusMillis(1500) +"'";
		

			ResultSet resultSet = this.statement.executeQuery(query);

			while(resultSet.next()) {	
				
				AnomalyDTO dto=new AnomalyDTO();
				
			
					
			
	
				dto.id=Integer.parseInt(resultSet.getString("id"));
				dto.dataId=Integer.parseInt(resultSet.getString("dataid"));
				dto.modelId=Integer.parseInt(resultSet.getString("modelid"));
				dto.label=Integer.parseInt(resultSet.getString("label"));
				dto.labelDate=resultSet.getString("labeltime");
				dto.rul=Double.parseDouble(resultSet.getString("rul"));
				dto.rulDate=resultSet.getString("rultime");
		
				dtoList.add(dto);
			}
			
			return dtoList;
						
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in read!");	
		}
		return null;		
	}

}
