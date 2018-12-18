package com.common.entity;

public class RepJson {

	private String code;
	private String message;
	private String note;
	private boolean success;
	
	
	public RepJson() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RepJson(String code, String message, String note, boolean success) {
		super();
		this.code = code;
		this.message = message;
		this.note = note;
		this.success = success;
	}
	/**  
	 * @Title:  isSuccess <BR>  
	 * @Description: please write your description <BR>  
	 * @return: boolean <BR>  
	 */
	public boolean isSuccess() {
		return success;
	}
	/**  
	 * @Title:  setSuccess <BR>  
	 * @Description: please write your description <BR>  
	 * @return: boolean <BR>  
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**  
	 * @Title:  getCode <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getCode() {
		return code;
	}
	/**  
	 * @Title:  setCode <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**  
	 * @Title:  getMessage <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getMessage() {
		return message;
	}
	/**  
	 * @Title:  setMessage <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**  
	 * @Title:  getNote <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getNote() {
		return note;
	}
	/**  
	 * @Title:  setNote <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**   
	 * <p>Title: toString</p>   
	 * <p>Description: </p>   
	 * @return   
	 * @see java.lang.Object#toString()   
	 */
	@Override
	public String toString() {
		return "RepJson [code=" + code + ", message=" + message + ", note=" + note + ", success=" + success + "]";
	}

}
