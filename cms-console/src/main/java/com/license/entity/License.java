package com.license.entity;


public class License {

	private String macAddress;
	private String notBefore;
	private String notAfter;
	private String licPath;
	private String subject;
	private String info;
	private String issuedTime;
	/**  
	 * @Title:  getMacAddress <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getMacAddress() {
		return macAddress;
	}
	/**  
	 * @Title:  setMacAddress <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	/**  
	 * @Title:  getNotBefore <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getNotBefore() {
		return notBefore;
	}
	/**  
	 * @Title:  setNotBefore <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setNotBefore(String notBefore) {
		this.notBefore = notBefore;
	}
	/**  
	 * @Title:  getNotAfter <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getNotAfter() {
		return notAfter;
	}
	/**  
	 * @Title:  setNotAfter <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setNotAfter(String notAfter) {
		this.notAfter = notAfter;
	}
	/**  
	 * @Title:  getLicPath <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getLicPath() {
		return licPath;
	}
	/**  
	 * @Title:  setLicPath <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setLicPath(String licPath) {
		this.licPath = licPath;
	}
	/**  
	 * @Title:  getSubject <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getSubject() {
		return subject;
	}
	/**  
	 * @Title:  setSubject <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**  
	 * @Title:  getInfo <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getInfo() {
		return info;
	}
	/**  
	 * @Title:  setInfo <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	/**  
	 * @Title:  getIssuedTime <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getIssuedTime() {
		return issuedTime;
	}
	/**  
	 * @Title:  setIssuedTime <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setIssuedTime(String issuedTime) {
		this.issuedTime = issuedTime;
	}
	/**   
	 * <p>Title: toString</p>   
	 * <p>Description: </p>   
	 * @return   
	 * @see java.lang.Object#toString()   
	 */
	@Override
	public String toString() {
		return "License [macAddress=" + macAddress + ", notBefore=" + notBefore + ", notAfter=" + notAfter
				+ ", licPath=" + licPath + ", subject=" + subject + ", info=" + info + ", issuedTime=" + issuedTime
				+ "]";
	}

	

}
