package helperClasses;


public class Creation {

	
	private String _name;
    private String _confidenceLevel;
    private String _keyword;
    private int _numberOfPlays;
    
    public Creation(String name) {
        this._name = name;
        this._numberOfPlays = 0;
        this._confidenceLevel = "0";
    }

    
    
    public String getName() {
        return _name;
    }

    public void setName(String str) {
        _name = str;
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
    
    public void increaseNumbeOfPlays() {
    	_numberOfPlays++;
    }
    
    
    public int getNumberOfPlays() {
    	return _numberOfPlays;
    }
    
}
