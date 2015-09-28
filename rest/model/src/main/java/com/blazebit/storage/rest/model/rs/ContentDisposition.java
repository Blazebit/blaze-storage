package com.blazebit.storage.rest.model.rs;

public class ContentDisposition {
	
	private static final String INLINE = "inline";
	private static final String ATTACHMENT = "attachment";
	
	private static final String PARAM_BEGIN = "; filename=\"";
	private static final String PARAM_END = "\"";
	
	private final String disposition;
	private final String filename;

	private ContentDisposition(String disposition, String filename) {
		this.disposition = disposition;
		
		if (filename == null) {
			this.filename = null;
		} else {
			filename = filename.trim();
			if (filename.isEmpty()) {
				this.filename = null;
			} else {
				this.filename = filename;
			}
		}
	}
	
	public String getDisposition() {
		return disposition;
	}

	public String getFilename() {
		return filename;
	}

	public static ContentDisposition inline(String filename) {
		return new ContentDisposition(INLINE, filename);
	}
	
	public static ContentDisposition attachment(String filename) {
		return new ContentDisposition(ATTACHMENT, filename);
	}
	
	public static ContentDisposition fromString(String string) {
		if (string == null || string.isEmpty()) {
			return null;
		}
		
		String disposition = null;
		if (string.regionMatches(0, INLINE, 0, INLINE.length())) {
			disposition = INLINE;
		} else if (string.regionMatches(0, ATTACHMENT, 0, ATTACHMENT.length())) {
			disposition = ATTACHMENT;
		}

		if (string.length() > disposition.length()) {
			if (string.regionMatches(disposition.length(), PARAM_BEGIN, 0, PARAM_BEGIN.length()) && string.endsWith(PARAM_END)) {
				String filename = string.substring(disposition.length() + PARAM_BEGIN.length(), string.length() - 1);
				
				if (!filename.isEmpty()) {
					return new ContentDisposition(disposition, filename);
				}
			}
		} else {
			return new ContentDisposition(disposition, null);
		}
		
		throw new IllegalArgumentException("Unexpected disposition: " + string);
	}

	@Override
	public String toString() {
		return disposition + (filename == null ? "" : PARAM_BEGIN + filename + PARAM_END);
	}
	
}
