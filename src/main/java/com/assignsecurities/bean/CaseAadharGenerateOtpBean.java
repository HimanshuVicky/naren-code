package com.assignsecurities.bean;

import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CaseAadharGenerateOtpBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6272036712215805876L;
	private Long caseId;
	private FileBean aadharFrontImage;
//	private FileBean aadharBackImage;

	public String toString() {
	    String lineSeaparator = System.getProperty("line.separator");
	    StringBuffer buffer = new StringBuffer(lineSeaparator);
	    buffer.append("|---------");
	    buffer.append(this.getClass());
	    buffer.append("---------|");
	    buffer.append(lineSeaparator);
	    Method[] methods = this.getClass().getMethods();
	    if(methods != null && methods.length >0){
	        Method method = null;
	        for(int i =0; i< methods.length; i++){
	            method = methods[i];
	            if(method.getName().startsWith("get")
	            && !method.getName().startsWith("getClass")){
	            buffer.append
	                           (method.getName().replaceAll("get",""));
	            buffer.append(" = ");
	            Object[] params=null;
	            try {
	                buffer.append
	                             (method.invoke(this, params));
	            } catch (Exception e) {
	                buffer.append("  ");
	            }
	            buffer.append(lineSeaparator);
	            }
	        }
	    }
	    buffer.append("|------------------------|");
	    buffer.append(lineSeaparator);
	    return buffer.toString();
	}
}
