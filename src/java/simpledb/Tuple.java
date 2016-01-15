package simpledb;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Tuple maintains information about the contents of a tuple. Tuples have a
 * specified schema specified by a TupleDesc object and contain Field objects
 * with the data for each field.
 */
public class Tuple implements Serializable {

    private static final long serialVersionUID = 1L;

    private TupleDesc m_td;
    private Field[] m_fields;
    private RecordId m_rid;
    /**
     * Create a new tuple with the specified schema (type).
     * 
     * @param td
     *            the schema of this tuple. It must be a valid TupleDesc
     *            instance with at least one field.
     */
    public Tuple(TupleDesc td) {
        // some code goes here
    	if( !(td instanceof TupleDesc))
    		throw new IllegalArgumentException("Not a TupleDesc");
    	if (td.numFields() < 1)
			throw new IllegalArgumentException("TupleDesc has less than 1 field");
    	m_td = td;
    	m_fields = new Field[td.numFields()];
    	
    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return m_td;
    }

    /**
     * @return The RecordId representing the location of this tuple on disk. May
     *         be null.
     */
    public RecordId getRecordId() {
        // some code goes here
        return m_rid;
    }

    /**
     * Set the RecordId information for this tuple.
     * 
     * @param rid
     *            the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        // some code goes here
    	m_rid = rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     * 
     * @param i
     *            index of the field to change. It must be a valid index.
     * @param f
     *            new value for the field.
     */
    public void setField(int i, Field f) {
        // some code goes here
    	if (i<0 || i>=m_fields.length)
    		throw new IllegalArgumentException("Invalid index");
    	m_fields[i] = f;
    }

    /**
     * @return the value of the ith field, or null if it has not been set.
     * 
     * @param i
     *            field index to return. Must be a valid index.
     */
    public Field getField(int i) {
        // some code goes here
    	if (i<0 || i>=m_fields.length)
    		throw new IllegalArgumentException("Invalid index");
    	return m_fields[i];
    }

    /**
     * Returns the contents of this Tuple as a string. Note that to pass the
     * system tests, the format needs to be as follows:
     * 
     * column1\tcolumn2\tcolumn3\t...\tcolumnN\n
     * 
     * where \t is any whitespace, except newline, and \n is a newline
     */
    public String toString() {
        // some code goes here
    	String text = "";
    	int i;
    	for(i=0; i<m_fields.length-1; i++){
    		text += m_fields[i] + "\t";
    	}
    	text += m_fields[i] + "\n";
        return text;
    }
    
    /**
     * @return
     *        An iterator which iterates over all the fields of this tuple
     * */
    public Iterator<Field> fields()
    {
        // some code goes here
    	return Arrays.asList(m_fields).iterator();
    }
    
    /**
     * reset the TupleDesc of the tuple
     * */
    public void resetTupleDesc(TupleDesc td)
    {
        // some code goes here
       	if( !(td instanceof TupleDesc))
    		throw new IllegalArgumentException("Not a TupleDesc");
    	if (td.numFields() < 1)
			throw new IllegalArgumentException("TupleDesc has less than 1 field");
    	m_td = td;
    	m_fields = new Field[td.numFields()];
    }
}
