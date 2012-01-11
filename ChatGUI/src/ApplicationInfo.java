public class ApplicationInfo{
	public String srvuser;
	public String clntuser;
	public String username;
	public String ipaddress;
	public String port;
	public String hash;
	public String role;

	public ApplicationInfo(){
		this.username = null;
		this.ipaddress = null;
		this.port = null;
		this.hash = null;
		this.role = null;
		
	}

	public ApplicationInfo(String username, String hash, String ipaddress, String port){
		this.username = username;
		this.hash = hash;
		this.ipaddress = ipaddress;
		this.port = port;
	}

	public ApplicationInfo(String username, String hash, String ipaddress, String port, String role){
		this.username = username;
		this.hash = hash;
		this.ipaddress = ipaddress;
		this.port = port;
		this.role = role;
	}

	public String getSrvUser(){
		return srvuser;
	}

	public String getClntUser(){
		return clntuser;
	}

	public void setClntUser(String newuser){
		clntuser = newuser; 
	}

	public void setSrvUser(String newuser){
		srvuser = newuser; 
	}

	public String getUsername(){
		return username;
	}

	public String getIpAdd(){
		return ipaddress;
	}

	public String getPort(){
		return port;
	}

	public String getHash(){
		return hash;
	}

	public String getRole(){
		return role;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public void setIpAdd(String ipaddress){
		this.ipaddress = ipaddress;
	}

	public void setPort(String port){
		this.port = port;
	}

	public void setHash(String hash){
		this.hash = hash;
	}

	public void setRole(String role){
		this.role = role;
	}
}
