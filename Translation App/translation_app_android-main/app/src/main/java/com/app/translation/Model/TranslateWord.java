package com.app.translation.Model;

public class TranslateWord {
    private String translation_id;
    private String user_id;
    private String from_type;
    private String from_text;
    private String to_type;
    private String to_text;
    private Boolean isSaved = false;

    public TranslateWord(){

    }

    public TranslateWord(String translation_id,String from_text,String to_text){
        this.translation_id = translation_id;
        this.user_id = "1";
        this.from_text = from_text;
        this.from_type = "en";
        this.to_text = to_text;
        this.to_type = "hi";
        this.isSaved = false;
    }


    public String getTranslationID() {
        return translation_id;
    }

    public void setTranslationID(String translation_id) {
        this.translation_id = translation_id;
    }

    public String getUserID() {
        return user_id;
    }

    public void setUserID(String user_id) {
        this.user_id = user_id;
    }

    public String getFromType() {
        return from_type;
    }

    public void setFromType(String from_type) {
        this.from_type = from_type;
    }

    public String getFromText() {
        return from_text;
    }

    public void setFromText(String from_text) {
        this.from_text = from_text;
    }

    public String getToType() {
        return to_type;
    }

    public void setToType(String to_type) {
        this.to_type = to_type;
    }

    public String getToText() {
        return to_text;
    }

    public void setToText(String to_text) {
        this.to_text = to_text;
    }

    public Boolean getSaved() {
        return isSaved;
    }

    public void setSaved(Boolean saved) {
        isSaved = saved;
    }
}
