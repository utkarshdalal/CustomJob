package buyer.post;

public class AttachmentItem {
	private String mTitle = null;
	private String mType = null;
	private byte[] mData = null;
	
	public AttachmentItem(String title, String type, byte[] data) {
		mTitle = title;
		mType = type;
		mData = data;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String getType() {
		return mType;
	}
	
	public byte[] getData() {
		return mData;
	}
}
