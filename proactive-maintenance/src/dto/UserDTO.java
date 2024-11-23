package dto;
import java.time.LocalDateTime;

public class UserDTO {



	    private int id;
	    private int userId;
	    private String username;
	    private LocalDateTime startTime;
	    private LocalDateTime endTime;

	    public UserDTO(String username2, LocalDateTime startTime2, LocalDateTime endTime2) {
			username=username2;
			startTime=startTime2;
			endTime=endTime2;
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

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	 
	    public LocalDateTime getStartTime() {
	        return startTime;
	    }

	    public void setStartTime(LocalDateTime startTime) {
	        this.startTime = startTime;
	    }

	    public LocalDateTime getEndTime() {
	        return endTime;
	    }

	    public void setEndTime(LocalDateTime endTime) {
	        this.endTime = endTime;
	    }
	}
