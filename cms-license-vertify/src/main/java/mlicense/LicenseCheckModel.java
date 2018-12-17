
package mlicense;

/**
 *  
 * @ClassName:  LicenseCheckModel   
 * @Description:truelicense extra  model
 * @author: Tyrone 
 * @date:   2018年12月17日 下午3:19:53   
 *     
 * @Copyright: 2018 www.hengyunsoft.com Inc. All rights reserved.
 */
public class LicenseCheckModel {

	private String ipAddress;
	
	private String ipMacAddress;
	
	private String CPUSerial;
	
	private String motherboardSN;
	
	private String hardDiskSN;



	/**  
	 * @Title:  getIpAddress <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getIpAddress() {
		return ipAddress;
	}



	/**  
	 * @Title:  setIpAddress <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	/**  
	 * @Title:  getIpMacAddress <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getIpMacAddress() {
		return ipMacAddress;
	}



	/**  
	 * @Title:  setIpMacAddress <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setIpMacAddress(String ipMacAddress) {
		this.ipMacAddress = ipMacAddress;
	}



	/**  
	 * @Title:  getCPUSerial <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getCPUSerial() {
		return CPUSerial;
	}



	/**  
	 * @Title:  setCPUSerial <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setCPUSerial(String cPUSerial) {
		CPUSerial = cPUSerial;
	}



	/**  
	 * @Title:  getMotherboardSN <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getMotherboardSN() {
		return motherboardSN;
	}



	/**  
	 * @Title:  setMotherboardSN <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setMotherboardSN(String motherboardSN) {
		this.motherboardSN = motherboardSN;
	}



	/**  
	 * @Title:  getHardDiskSN <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getHardDiskSN() {
		return hardDiskSN;
	}



	/**  
	 * @Title:  setHardDiskSN <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setHardDiskSN(String hardDiskSN) {
		this.hardDiskSN = hardDiskSN;
	}



	/**   
	 * <p>Title: toString</p>   
	 * <p>Description: </p>   
	 * @return   
	 * @see java.lang.Object#toString()   
	 */
	@Override
	public String toString() {
		return "LicenseCheckModel [ipAddress=" + ipAddress + ", ipMacAddress=" + ipMacAddress + ", CPUSerial="
				+ CPUSerial + ", motherboardSN=" + motherboardSN + ", hardDiskSN=" + hardDiskSN + "]";
	}
	
	

}
