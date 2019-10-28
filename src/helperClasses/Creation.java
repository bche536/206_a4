package helperClasses;


import java.io.Serializable;

public class Creation implements Serializable {

	
	private String _name;
    private String _confidenceLevel;
    private String _keyword;
    private int _numberOfPlays;

    /**
     * Creates a creation obj with the the name and keyword provided
     * @param name
     * @param keyword
     */
    public Creation(String name, String keyword) {
        _name = name;
        _keyword = keyword;
        _numberOfPlays = 0;
        _confidenceLevel = "0";
    }


    /**
     * Gets the creation name
     * @return
     */
    public String getName() {
        return _name;
    }

    /**
     * Gets the confidence level
     * @return
     */
    public String getConfidenceLevel() {
        return _confidenceLevel;
    }

    /**
     * Sets the confidence level
     * @param level
     */
    public void setConfidence(String level) {
    	_confidenceLevel=level;
    }

    /**
     * Gets the keyword
     * @return
     */
    public String getKeyword() {
        return _keyword;
    }

    /**
     * Increments the # of plays
     */
    public void increaseNumberOfPlays() {
    	_numberOfPlays++;
    }

    /**
     * Gets the number of plays
     * @return
     */
    public int getNumberOfPlays() {
    	return _numberOfPlays;
    }

}
