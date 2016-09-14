package com.myservices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/file")
public class UploadFileService {

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		String uploadedFileLocation = "d://uploaded/" + fileDetail.getFileName();
		// save it
		Object responseUrl = writeToFile(uploadedInputStream, uploadedFileLocation);
		return Response.status(200).entity(responseUrl).build();

	}

	// save uploaded file to new location
	private Object writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
		Object cloudinaryUrl = "Failed to upload";
		try {
			File file = new File(uploadedFileLocation);
			file.createNewFile();
			OutputStream out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			cloudinaryUrl = uploadToCloudinary(file);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return cloudinaryUrl;

	}

	private Object uploadToCloudinary(File file) {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "<your>", "api_key",
				"<your>", "api_secret", "<your>"));
		try {
			Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
			return uploadResult.get("secure_url");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
