package dto;

public class UserTaskDTO {



	    private int id;
	    private int userId;
	    private int taskId;
	    private double responseTime;
	    private int taskQuestionNumber;
	    

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


	    public int getTaskId() {
	        return taskId;
	    }

	    public void setTaskId(int taskId) {
	        this.taskId = taskId;
	    }

	    public double getResponseTime() {
	        return responseTime;
	    }

	    public void setResponseTime(double responseTime) {
	        this.responseTime = responseTime;
	    }
	    
	    public int getTaskQuestionNumber() {
	        return taskQuestionNumber;
	    }

	    public void setTaskQuestionNumber(int taskQuestionNumber) {
	        this.taskQuestionNumber = taskQuestionNumber;
	    }

	   
	}
