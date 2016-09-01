package com.civfactions.SabreCore;

import com.civfactions.SabreApi.data.DataCollection;
import com.civfactions.SabreApi.data.FieldUpdate;
import com.civfactions.SabreApi.util.Guard;

public class BlockUpdate implements FieldUpdate<CoreBlock> {
	
	private final ChunkBlocks chunks;
	private final DataCollection<ChunkBlocks> data;
	
	BlockUpdate(final ChunkBlocks chunks, final DataCollection<ChunkBlocks> data) {
		Guard.ArgumentNotNull(chunks, "chunks");
		Guard.ArgumentNotNull(data, "data");
		
		this.chunks = chunks;
		this.data = data;
	}
	
	@Override
	public void updateField(final CoreBlock b, final String key, final Object value) {
		data.updateField(chunks, String.format("blocks.%s.%s", b.getDocumentKey(), key), value);
	}
}
