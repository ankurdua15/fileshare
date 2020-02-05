package com.fileshare.fileshare;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

@Controller
public class FileShareController {

	String directory;
	FileShareController(){
		directory=System.getProperty("user.home");
	}

	@GetMapping("/list")
	public ResponseEntity<String> list(){
		String ret="";
		File dir=new File(directory);
		File files[]=dir.listFiles();
		for(File file:files){
			ret+=(file.getName()+" "+(new Date(file.lastModified()).toString()))+"\n";
		}
		if(files==null)ret="An error occured\n";
		if(files.length==0)ret="No files to display\n";
		return  ResponseEntity.ok(ret);
	}

	@PutMapping(value = "/{fileName}")
	public ResponseEntity<String> upload(@PathVariable(value = "fileName") String fileName, HttpServletRequest request){


		String fileNames[]=new File(directory).list();
		for(String fileNameD:fileNames){
			if(fileNameD.equals(fileName))
				return ResponseEntity.ok("File already exists\n");
		}


		try{
			InputStream inputStream = request.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(new File(directory+"/"+fileName));

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.flush();
			return ResponseEntity.ok("File Uploaded Successfully\n");
		}catch(Exception e){
			return ResponseEntity.ok("An error occured "+e.getMessage()+"\n");
		}
	}

	@GetMapping("/{fileName}")
	public ResponseEntity<String> getFile(@PathVariable(value="fileName") String fileName){
		File file=new File(directory+"/"+fileName);
		if(!file.exists())
			return ResponseEntity.ok("File does not exist.\n");
		try{
			FileInputStream fileInputStream=new FileInputStream(file);
			byte buf[]=new byte[(int)file.length()];
			fileInputStream.read(buf);
			String fileCont=new String(buf);
			return ResponseEntity.ok(fileCont);
		}catch(Exception e){
			return ResponseEntity.ok("An error occured "+e.getMessage()+"\n");
		}
	}

}
