package com.assignsecurities.dm;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.assignsecurities.domain.dm.UploadItem;


public class FileValidator implements Validator {

	public boolean supports(Class<?> arg0) {
		return false;
	}

	public void validate(Object uploadItem, Errors errors) {
		UploadItem file = (UploadItem) uploadItem;

		if (file == null || file.getFileData() == null
				|| file.getFileData().getSize() == 0) {
			errors.rejectValue("fileData", "uploadItem.salectFile",
					"Please select a file!");
		}else{
			String fileName=file.getFileData().getOriginalFilename();
			String fileExt =fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
			if(fileExt.equalsIgnoreCase("xlsx")){
				errors.rejectValue("fileData", "uploadItem.salectFile",
						"Please select a valid file(xlsx)!");
			}
		}
	}

}
