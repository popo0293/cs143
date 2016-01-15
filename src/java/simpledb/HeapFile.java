package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {

    /**
     * Constructs a heap file backed by the specified file.
     * 
     * @param f
     *            the file that stores the on-disk backing store for this heap
     *            file.
     */
	private File m_file;
	private TupleDesc m_td;
	private int m_hfid;
	
    public HeapFile(File f, TupleDesc td) {
        // some code goes here
    	m_file = f;
    	m_td = td;
    	m_hfid = f.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the File backing this HeapFile on disk.
     * 
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        return m_file;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     * 
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        // some code goes here
    	return m_hfid;
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * 
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
    	return m_td;
    }

    // see DbFile.java for javadocs
    public Page readPage(PageId pid) {
        // some code goes here
    	HeapPageId hpid = (HeapPageId)pid;
        try {
            long offset = BufferPool.PAGE_SIZE * hpid.pageNumber();
            byte[] b = new byte[BufferPool.PAGE_SIZE];
            if (offset + BufferPool.PAGE_SIZE > m_file.length())
            	throw new IllegalArgumentException();
            RandomAccessFile raf = new RandomAccessFile(m_file,"r");
            raf.seek(offset);
            raf.read(b);
            raf.close();
            return new HeapPage(hpid, b);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        } catch (IOException e) {
            throw new IllegalArgumentException("Page out of bound");
        }
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // some code goes here
        // not necessary for lab1
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        // some code goes here
        return (int)((m_file.length()+BufferPool.PAGE_SIZE-1)/BufferPool.PAGE_SIZE);
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
        // some code goes here
        return new HeapFileIterator(tid);
    }

    
    public class HeapFileIterator implements DbFileIterator {

    	private TransactionId m_tid;
    	private int pgNo = 0;
    	private Iterator<Tuple> m_tupleItr;
    	private boolean opened = false;
    	
    	public HeapFileIterator(TransactionId tid) {
    		m_tid = tid;
    		opened = false;
    	}

    	@Override
    	public void open() throws DbException, TransactionAbortedException {
    		if (opened)
    			return;
    		m_tupleItr = getListFromPage(pgNo);
    		opened = true;
    	}

    	@Override
    	public boolean hasNext() throws DbException, TransactionAbortedException {
    		if (!opened || m_tupleItr == null)
    			return false;
    		if (m_tupleItr.hasNext() || (pgNo < numPages()-1))
    			return true;
    		return false;

    	}

    	@Override
    	public Tuple next() throws DbException, TransactionAbortedException,
    			NoSuchElementException {
    		if (this.hasNext())
    			if(m_tupleItr.hasNext())
    				return m_tupleItr.next();
    			else{
    				pgNo += 1;
    				m_tupleItr = getListFromPage(pgNo);
    				if (m_tupleItr.hasNext())
    					return m_tupleItr.next();
    				else
    					throw new NoSuchElementException();
    			}
    		throw new NoSuchElementException();
    	}

    	@Override
    	public void rewind() throws DbException, TransactionAbortedException {
    		close();
    		pgNo = 0;
    		open();
    	}

    	@Override
    	public void close() {
    		//m_tupleItr = null;
    		opened = false;
    	}

    	private Iterator<Tuple> getListFromPage(int pgNo)  throws DbException, TransactionAbortedException {
    		PageId pid = new HeapPageId(getId(), pgNo);
    		HeapPage hp = (HeapPage) Database.getBufferPool().getPage(m_tid, pid, Permissions.READ_WRITE);
    		return hp.iterator();
    	}
    	
    }
}

