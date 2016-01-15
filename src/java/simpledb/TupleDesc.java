package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

	private Vector<TDItem> m_td_vec;
    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
        return m_td_vec.iterator();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
    	m_td_vec = new Vector<TDItem>(typeAr.length);
    	int i;
    	for (i = 0; i < typeAr.length; i++) {
    		m_td_vec.add(new TDItem(typeAr[i],fieldAr[i]));
    	}
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
    	m_td_vec = new Vector<TDItem>(typeAr.length);
    	int i;
    	for (i = 0; i < typeAr.length; i++) {
    		m_td_vec.add(new TDItem(typeAr[i],null));
    	}
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return m_td_vec.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
    	if ( i<0 || i>=m_td_vec.size() )
    		throw new NoSuchElementException();
        return m_td_vec.get(i).fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
    	if ( i<0 || i>=m_td_vec.size() )
    		throw new NoSuchElementException();
        return m_td_vec.get(i).fieldType;
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {

    	if (name != null)
	    	for (TDItem field : m_td_vec)
	    		if (name.equals(field.fieldName))
	    			return m_td_vec.indexOf(field);
     	throw new NoSuchElementException("No matching name found.");

    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
    	int size = 0;
    	for (TDItem field : m_td_vec){
    		size += field.fieldType.getLen();
    	}
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
    	 int length = td1.numFields() + td2.numFields();
    	 Type[] typeAr = new Type[length];
         String[] fieldAr = new String[length];
         int i,j;
         for (i = 0; i < td1.numFields(); i++){
        	 typeAr[i] = td1.getFieldType(i);
        	 fieldAr[i] = td1.getFieldName(i);
         }
         for (j=0 ;i<length;j++,i++){
        	 typeAr[i] = td2.getFieldType(j);
        	 fieldAr[i] = td2.getFieldName(j);
         }
        return new TupleDesc(typeAr,fieldAr);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        // some code goes here
    	if (o instanceof TupleDesc){
    		TupleDesc td = (TupleDesc) o;
    		int nFields = td.numFields();
    		if (nFields != this.numFields() || td.getSize() != this.getSize())
    			return false;
    		for(int i=0; i<nFields; i++)
    		{
    			if(td.getFieldType(i) != this.getFieldType(i))
    				return false;
    		}
    		return true;
    	}
        return false;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        return toString().hashCode();
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
    	String text = "";
    	int i = 0;
    	for(i=0; i<this.numFields()-1; i++){
    		text += m_td_vec.get(i).fieldType+"["+i+"]("+m_td_vec.get(i).fieldName+"),";
    	}
    	text += m_td_vec.get(i).fieldType+"["+i+"]("+m_td_vec.get(i).fieldName+")";
        return text;
    }
}
