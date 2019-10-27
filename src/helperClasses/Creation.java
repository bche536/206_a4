package helperClasses;


import java.io.Serializable;

public class Creation implements Serializable {

	
	private String _name;
    private String _confidenceLevel;
    private String _keyword;
    private int _numberOfPlays;
    
    public Creation(String name, String keyword) {
        _name = name;
        _keyword = keyword;
        _numberOfPlays = 0;
        _confidenceLevel = "0";
    }

    
    
    public String getName() {
        return _name;
    }

    public String getConfidenceLevel() {
        return _confidenceLevel;
    }

    public void setConfidence(String level) {
    	_confidenceLevel=level;
    }
    
    public String getKeyword() {
        return _keyword;
    }

    public void setKeyword(String keyword) {
        _keyword=keyword;
    }
    
    public void increaseNumberOfPlays() {
    	_numberOfPlays++;
    }

    public int getNumberOfPlays() {
    	return _numberOfPlays;
    }

}
