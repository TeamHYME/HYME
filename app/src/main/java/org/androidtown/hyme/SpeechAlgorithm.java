package org.androidtown.hyme;

/*
 * Analyze text and figure it as speech type
 */
public class SpeechAlgorithm {

    private int type=0;

    public SpeechAlgorithm(){}

    public void setType(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    // Predict type via text
    public int analyzeText(String text){

        String trimmed = text.trim();
        String analyzed="";

        if(trimmed.length() >= 15) {
            analyzed = trimmed.substring(0, 15);
        }
        else{
            analyzed = trimmed.substring(0, trimmed.length());
        }

        // predict etc. as former type didn't select yet
        if(type == 0){
            return 6;
        }

        // predict as opinion
        if(analyzed.contains("제 생각은") || analyzed.contains("제 입장은") || analyzed.contains("와 다르게") || analyzed.contains("의견")){
            return 1;
        }

        // predict as question
        if(analyzed.contains("질문") || analyzed.contains("여쭤볼게") || analyzed.contains("물어볼게")){
            return 2;
        }

        // predict as refutation
        if(analyzed.contains("하지만") || analyzed.contains("의문") || analyzed.contains("이지만") || analyzed.contains("그러나") || analyzed.contains("그런데")){
            return 4;
        }

        // predict as additional content
        if (analyzed.contains("덧붙여서") || analyzed.contains("추가로") || analyzed.contains("추가")) {
            return 5;
        }

        // predict etc. when speaker said about meeting begins or ends
        if( analyzed.contains("안녕하세요")||(analyzed.contains("회의") && analyzed.contains("시작")) ||
                (analyzed.contains("회의") && (analyzed.contains("마치") || analyzed.contains("끝")))){
            return 6;
        }

        // Uncertain text just defined as additional content
        return 5;

    }

    // Predict type via former type
    public int analyzeType(){

        // predict as answer if the former type was question or refutation
        if((type == 2) || (type == 4)){
            return 3;
        }

        // change to etc. as the answer was over
        if(type == 3){
            return 0;
        }

        return 0;
    }

}
