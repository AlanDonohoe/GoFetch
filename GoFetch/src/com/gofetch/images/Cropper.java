package com.gofetch.images;

import org.primefaces.model.CroppedImage;

public class Cropper {
	
	private CroppedImage croppedImage;
	private String newImageName;
	
	public Cropper(){}

	public CroppedImage getCroppedImage() {
		return croppedImage;
	}

	public void setCroppedImage(CroppedImage croppedImage) {
		this.croppedImage = croppedImage;
	}

	public String getNewImageName() {
		return newImageName;
	}

	public void setNewImageName(String newImageName) {
		this.newImageName = newImageName;
	}
	
}
