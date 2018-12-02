package MapReduce;

import Main.Term;

public interface ITermDocumentInfo extends Info {

    public int getFrequency();
    public Term getTerm();
    public String getDocumentID();
    public void addToFrequency(int frequency);
    public void setFrequency(int frequency);

    @Override
    public String toString();

}
