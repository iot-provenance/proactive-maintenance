package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import dto.*;

public class DataReaderLite {	

	private static String[] head = {"Square id","Time Interval",
			"SMS-in activity","SMS-out activity",
			"Call-in activity","Call-out activity:",
			"Internet traffic activity"};
	private String lastRow;
	
	private final String url = "jdbc:sqlite:D:\\DB\\dataset.db";
	
	//private final String url = "jdbc:sqlite:C:\\Users\\elpaem\\Google Drive\\YTU\\IoT\\CODE\\Prov\\DB\\dataset.db";
	
	
	private Connection connection;
	private Statement statement;
	private int dataCount = 1;
	
	
	public String[] getHead() {
		return head;
	}	

	public String getLastRow() {
		return lastRow;
	}
	
	public DataReaderLite(int dataStart){
		dataStart=1;
		this.dataCount = dataStart+1;
		try{
			if(this.connection==null)
					this.connection = DriverManager.getConnection(url);			
			if(connection != null) {
				System.out.println("Connected to DB-DataReaderLite");
			}else {
				System.out.println("DB connection failed");
			}
			
			this.statement = getConnection().createStatement();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public double[] readData(){
		try {


		
			String query = "select * from (\r\n"
					+ "SELECT  mid, cycle,coalesce(error, true)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid\r\n"
					+ "	FROM nasa where did =4) as sub\r\n"
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
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, id \r\n"
					+ "	FROM nasa where did in ("+did+") order by mid, cycle" ;
			this.dataCount++;

			ResultSet resultSet = this.statement.executeQuery(query);

			int k=0;
			ArrayList<double[]> result=new ArrayList<double[]>();
			while(resultSet.next()) {	
				
				DatasetDTO dto=new DatasetDTO();
				
		

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
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, id, CASE WHEN (SELECT count(l.dataid)\r\n"
					 + "	FROM label l where l.modelid in (4,5) and l.dataid=nasa.id )>0 THEN 1 ELSE 0 END as label		FROM nasa\r\n"
					 + "	where did ="+did+" and state='"+state+"' order by mid, cycle limit "+limit+"" ;
			
			if(state=="") {
			 query = 
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, CASE WHEN (SELECT count(l.dataid)\r\n"
					 + "	FROM label l where l.modelid in (4,5) and l.dataid=nasa.id )>0 THEN 1 ELSE 0 END as label		FROM nasa\r\n"
					 + "	where did ="+did+" order by mid, cycle limit "+limit+"" ;
			
			}
			 this.dataCount++;

			ResultSet resultSet = this.statement.executeQuery(query);

			int k=0;
			ArrayList<double[]> result=new ArrayList<double[]>();
			while(resultSet.next()) {	
				
				DatasetDTO dto=new DatasetDTO();
				
			
						
				String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
						resultSet.getString(20),resultSet.getString(21),resultSet.getString(23),
						resultSet.getString(1),resultSet.getString(2)};
				

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
			
			this.statement.close();
			this.connection.close();
			
			return dtoList;
						
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in read!");	
			try {
				this.connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;		
	}
	
	public ArrayList<DatasetDTO> readLabeledDataList(int did, int modelID, String state, int limit){
		try {

			ArrayList<DatasetDTO> dtoList=new ArrayList<DatasetDTO>();
		

			
			String query = 
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, id, coalesce(l.label, 0) label	FROM nasa\r\n"
					 + "	left join label  l on l.dataid=id and l.modelid="+modelID+" where did ="+did+" and state='"+state+"' order by mid, cycle limit "+limit+"" ;
			
			if(state=="") {
			 query = 
					 "SELECT  mid, cycle,coalesce(error, false)  , s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s22, s23, s24,ROW_NUMBER () OVER (ORDER BY cycle,mid) as sid, id, coalesce(l.label, 0) label	FROM nasa\r\n"
					 + "	left join label  l on l.dataid=id and l.modelid="+modelID+" where did ="+did+" order by mid, cycle limit "+limit+"" ;
			
			}
			 this.dataCount++;

			ResultSet resultSet = this.statement.executeQuery(query);

			int k=0;
			ArrayList<double[]> result=new ArrayList<double[]>();
			while(resultSet.next()) {	
				
				DatasetDTO dto=new DatasetDTO();
				
			
						
				String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
						resultSet.getString(20),resultSet.getString(21),resultSet.getString(23),
						resultSet.getString(1),resultSet.getString(2)};
				

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
		
		String query="INSERT INTO label(\r\n"
				+ " dataid, modelid, label, labeltime)\r\n"
				+ "	VALUES ( "+dataId+", "+modelId+", 1, datetime('now'));";
		
		try {
			this.statement.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertAnomaly(int dataId, int modelId, double label) {
		String query="INSERT INTO anomaly(\r\n"
				+ " dataid, modelid, label, labeltime)\r\n"
				+ "	VALUES ( "+dataId+", "+modelId+", 1, datetime('now')); update nasa set state='anomaly' where id="+dataId;
		
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
					+ "	FROM nasa where did =4 ) as sub\r\n"
					+ "	where sub.sid<="+ max +";";
			this.dataCount++;

			ResultSet resultSet = this.statement.executeQuery(query);

			int k=0;
			ArrayList<double[]> result=new ArrayList<double[]>();
			while(resultSet.next() && k<max) {	
				k++;
				boolean isError =  resultSet.getBoolean(3);
	
					
				String[] row = {  resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(8),resultSet.getString(9),
						resultSet.getString(10),resultSet.getString(13),resultSet.getString(14),
						resultSet.getString(15),resultSet.getString(17),resultSet.getString(18),resultSet.getString(19),
						resultSet.getString(20),resultSet.getString(21),resultSet.getString(23),
						resultSet.getString(1),resultSet.getString(2)};
				
				
				
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

	public void closeReader(){
		
		try {
			getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			System.out.println(e);	
			System.out.println("Unnown error in close!");	
		}
	}
	
	public ArrayList<AnomalyDTO>  getAnomaly(int dataId) {
		// TODO Auto-generated method stub
		try {

			ArrayList<AnomalyDTO> dtoList=new ArrayList<AnomalyDTO>();
		

			
			String query ="SELECT id,dataid,modelid,label,labeltime,rul,rultime from anomaly where dataid ="+dataId ;
		

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
		

			
			String query ="SELECT a.id,a.dataid,a.modelid,a.label,a.labeltime,a.rul,a.rultime from anomaly  a inner join nasa n on a.dataid=n.id  where n.state='rul' and rultime> '"+ Instant.now().minusMillis(1500) +"'";
		

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

	public void insertUserResponse(UserResponseDTO response) throws SQLException {
        String sql = "INSERT INTO userResponse (user_id, question_id, response, questionIndex) VALUES (?, ?, ?, ?)";

        try (
             PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, response.getUserId());
              pstmt.setInt(2, response.getQuestionId());
            pstmt.setInt(3, response.getResponse());
            pstmt.setInt(4, response.getQuestionIndex());
            pstmt.executeUpdate();
            
            this.connection.close();
        }
    }
	
	public void insertUserTask(UserTaskDTO task) throws SQLException {
        String sql = "INSERT INTO userTask (user_id, task_id, responseTime, task_question_number) VALUES (?, ?, ?, ?)";

        try (
             PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, task.getUserId());
              pstmt.setInt(2, task.getTaskId());
            pstmt.setDouble(3, task.getResponseTime());
            pstmt.setInt(4, task.getTaskQuestionNumber());
            pstmt.executeUpdate();
            
            this.connection.close();
        }
    }

	public Connection getConnection() {

		
		
			try {
				if(this.connection==null || this.connection.isClosed() ) {
					try {
						this.connection = DriverManager.getConnection(url);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
	
		return this.connection;
	}
	
    public int insertUser(UserDTO user) throws SQLException {
        String sql = "INSERT INTO user (username, starttime, endtime) VALUES (?, ?, ?)";
        int generatedId = -1;

        try (
        		
             PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setObject(2, user.getStartTime());
            pstmt.setObject(3, user.getEndTime());

            pstmt.executeUpdate();
           
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        
        pstmt.close();
        }
        return generatedId;
    }

}
