package edi.parser.message.segment;

import edi.parser.message.adapter.IntegerAdapter;
import edi.parser.message.common.SegmentElement;
import edi.parser.engine.SegmentDeclarationTypeA;

@SegmentDeclarationTypeA(name = "UNT")
public class MessageTrailerSegment extends AbstractSegment {
    @SegmentElement(adapter = IntegerAdapter.class)
    private Integer segmentCount;

    @SegmentElement()
    private String referenceNumber;

    @SegmentElement(nextGroup = true)
    private String type;

    public Integer getSegmentCount() {
        return segmentCount;
    }

    public void setSegmentCount(Integer segmentCount) {
        this.segmentCount = segmentCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }


}
