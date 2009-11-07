package fellas;

import java.io.Serializable;
import java.util.ArrayList;

// server class
public class Club implements Serializable {
	private String oName;
	private String oSurname;
	private String cAddress;
	private String cTel;
	private String cEMail;
	private String cType;
	private String cName;
	private String psw;

	ArrayList<MyEvent> eventList = new ArrayList<MyEvent>();

	public Club(String oName, String oSurname, String cAddress, String cTel,
			String cName, String psw) {
		super();
		this.oName = oName;
		this.oSurname = oSurname;
		this.cAddress = cAddress;
		this.cTel = cTel;
		this.cEMail = cEMail;
		this.cType = cType;
		this.cName = cName;
		this.psw = psw;
	}

	public Club() {
		super();
		this.oName = "";
		this.oSurname = "";
		this.cAddress = "";
		this.cTel = "";
		this.cEMail = "";
		this.cType = "";
		this.cName = "";
		this.psw = "";
	}

	public String getoName() {
		return oName;
	}

	public void setoName(String oName) {
		this.oName = oName;
	}

	public String getoSurname() {
		return oSurname;
	}

	public void setoSurname(String oSurname) {
		this.oSurname = oSurname;
	}

	public String getcAddress() {
		return cAddress;
	}

	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}

	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	public String getcEMail() {
		return cEMail;
	}

	public void setcEMail(String cEMail) {
		this.cEMail = cEMail;
	}

	public String getcTel() {
		return cTel;
	}

	public void setcTel(String cTel) {
		this.cTel = cTel;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String toString() {
		return "Club [cAddress=" + cAddress + ", cEMail=" + cEMail + ", cName="
				+ cName + ", cTel=" + cTel + ", cType=" + cType + ", oName="
				+ oName + ", oSurname=" + oSurname + ", psw=" + psw + "]";
	}
}
