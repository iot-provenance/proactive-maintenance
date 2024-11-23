package dto;
import java.time.LocalDateTime;

public class UserResponseDTO {



	    private int id;
	    private int userId;
	    private int questionId;
	    private int response;
	    private int questionIndex;
	    
	    public UserResponseDTO(int i, int j, int k) {
	    	questionIndex=i;
	    	questionId=j;
	    	response=k;
			
		}

		// Getters and Setters
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public int getUserId() {
	        return userId;
	    }

	    public void setUserId(int userId) {
	        this.userId = userId;
	    }


	    public int getQuestionId() {
	        return questionId;
	    }

	    public void setQuestionId(int questionId) {
	        this.questionId = questionId;
	    }

	    public int getResponse() {
	        return response;
	    }

	    public void setResponse(int response) {
	        this.response = response;
	    }
	    
	    public int getQuestionIndex() {
	        return questionIndex;
	    }

	    public void setQuestionIndex(int index) {
	        this.questionIndex = index;
	    }

	   
	}
