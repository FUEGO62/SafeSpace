package com.bytebuilder.dto.response.walrus;


//import lombok.Getter;
//import lombok.Setter;


public class WalrusUploadResponse {

    public NewlyCreated getNewlyCreated() {
        return newlyCreated;
    }

    public void setNewlyCreated(NewlyCreated newlyCreated) {
        this.newlyCreated = newlyCreated;
    }

    public AlreadyCertified getAlreadyCertified() {
        return alreadyCertified;
    }

    public void setAlreadyCertified(AlreadyCertified alreadyCertified) {
        this.alreadyCertified = alreadyCertified;
    }

    private NewlyCreated newlyCreated;
    private AlreadyCertified alreadyCertified;
}
