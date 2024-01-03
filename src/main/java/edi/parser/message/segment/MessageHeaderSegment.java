package edi.parser.message.segment;

import edi.parser.engine.SegmentDeclarationTypeA;
import edi.parser.message.adapter.IntegerAdapter;
import edi.parser.message.adapter.StringAdapter;
import edi.parser.message.common.SegmentElement;

@SegmentDeclarationTypeA(name = "UNH")
public class MessageHeaderSegment extends AbstractSegment {
    @SegmentElement(adapter = StringAdapter.class, required = true, code = "")
    private String referenceNumber;

    /**
     * First element of meeage ID
     */
    @SegmentElement(adapter = StringAdapter.class, required = true, nextGroup = true, code = "")
    private String type;

    @SegmentElement(code = "")
    private String versionNumber;

    @SegmentElement(adapter = IntegerAdapter.class, /*required = true, */code = "")
    private int realiaseNumber;

    @SegmentElement(adapter = StringAdapter.class, /*required = true, */code = "")
    private String controlingAgency;
    @SegmentElement(nextGroup = true)
    private String commonAccessReference;

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getRealiaseNumber() {
        return realiaseNumber;
    }

    public void setRealiaseNumber(int realiaseNumber) {
        this.realiaseNumber = realiaseNumber;
    }

    public String getControlingAgency() {
        return controlingAgency;
    }

    public void setControlingAgency(String controlingAgency) {
        this.controlingAgency = controlingAgency;
    }

    public String getCommonAccessReference() {
        return commonAccessReference;
    }

    public void setCommonAccessReference(String commonAccessReference) {
        this.commonAccessReference = commonAccessReference;
    }
}
