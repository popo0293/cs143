package simpledb;

import java.util.*;

/**
 * SeqScan is an implementation of a sequential scan access method that reads
 * each tuple of a table in no particular order (e.g., as they are laid out on
 * disk).
 */
public class SeqScan implements DbIterator {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a sequential scan over the specified table as a part of the
     * specified transaction.
     * 
     * @param tid
     *            The transaction this scan is running as a part of.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    private TransactionId m_tid;
    private int m_tableId;
    private String m_tbAlias;
    private DbFileIterator m_dbItr;
    
    public SeqScan(TransactionId tid, int tableid, String tableAlias) {
        // some code goes here
    	m_tid = tid;
    	m_tableId = tableid;
    	m_tbAlias = tableAlias;
    }

    /**
     * @return
     *       return the table name of the table the operator scans. This should
     *       be the actual name of the table in the catalog of the database
     * */
    public String getTableName() {
        return Database.getCatalog().getTableName(m_tableId);
    }
    
    /**
     * @return Return the alias of the table this operator scans. 
     * */
    public String getAlias()
    {
        // some code goes here
        return m_tbAlias;
    }

    /**
     * Reset the tableid, and tableAlias of this operator.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    public void reset(int tableid, String tableAlias) {
        // some code goes here
    	m_tableId = tableid;
    	m_tbAlias = tableAlias;
    }

    public SeqScan(TransactionId tid, int tableid) {
        this(tid, tableid, Database.getCatalog().getTableName(tableid));
    }

    public void open() throws DbException, TransactionAbortedException {
        // some code goes here
    	m_dbItr = Database.getCatalog().getDatabaseFile(m_tableId).iterator(m_tid);
    	m_dbItr.open();
    	
    }

    /**
     * Returns the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor. This prefix
     * becomes useful when joining tables containing a field(s) with the same
     * name.
     * 
     * @return the TupleDesc with field names from the underlying HeapFile,
     *         prefixed with the tableAlias string from the constructor.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        TupleDesc tmp = Database.getCatalog().getTupleDesc(m_tableId);
        int len = tmp.numFields();
        Type[] td_types = new Type[len];
        String[] td_fields = new String[len];
        for (int i = 0; i < len; i++) {
        	td_types[i] = tmp.getFieldType(i);
        	td_fields[i] = m_tbAlias+"."+tmp.getFieldName(i);
        }
        return new TupleDesc(td_types,td_fields);
    }

    public boolean hasNext() throws TransactionAbortedException, DbException {
        // some code goes here
        if(m_dbItr != null)
        	return m_dbItr.hasNext();
    	return false;
    }

    public Tuple next() throws NoSuchElementException,
            TransactionAbortedException, DbException {
        // some code goes here
    	if (hasNext())
    		return m_dbItr.next();
    	throw new NoSuchElementException();
    }

    public void close() {
        // some code goes here
    	m_dbItr.close();
    }

    public void rewind() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        // some code goes here
    	m_dbItr.rewind();
    }
}
