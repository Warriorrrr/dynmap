package org.dynmap.bukkit.helper.v116_4;

import net.minecraft.server.v1_16_R3.*;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.dynmap.DynmapChunk;
import org.dynmap.bukkit.helper.BukkitWorld;
import org.dynmap.common.chunk.GenericChunk;
import org.dynmap.common.chunk.GenericChunkCache;
import org.dynmap.common.chunk.GenericMapChunkCache;

import java.io.IOException;
import java.util.List;

/**
 * Container for managing chunks - dependent upon using chunk snapshots, since rendering is off server thread
 */
public class MapChunkCache116_4 extends GenericMapChunkCache {
	private World w;
	/**
	 * Construct empty cache
	 */
	public MapChunkCache116_4(GenericChunkCache cc) {
		super(cc);
	}

	// Load generic chunk from existing and already loaded chunk
	protected GenericChunk getLoadedChunk(DynmapChunk chunk) {
        CraftWorld cw = (CraftWorld) w;
        NBTTagCompound nbt = null;
        GenericChunk gc = null;
        if (cw.isChunkLoaded(chunk.x, chunk.z)) {
            Chunk c = cw.getHandle().getChunkAt(chunk.x,  chunk.z);
            if ((c != null) && c.loaded) {
                nbt = ChunkRegionLoader.saveChunk(cw.getHandle(), c);
            }
            if (nbt != null) {
            	gc = parseChunkFromNBT(new NBT.NBTCompound(nbt));
            }
        }
    	return gc;
	}
	
	// Load generic chunk from unloaded chunk
	protected GenericChunk loadChunk(DynmapChunk chunk) {
        CraftWorld cw = (CraftWorld) w;
        NBTTagCompound nbt = null;
        ChunkCoordIntPair cc = new ChunkCoordIntPair(chunk.x,  chunk.z);
        GenericChunk gc = null;
        try {
            nbt = cw.getHandle().getChunkProvider().playerChunkMap.read(cc);
        } catch (IOException iox) {
        }
        if (nbt != null) {
        	gc = parseChunkFromNBT(new NBT.NBTCompound(nbt));
        }
    	return gc;
	}

	public void setChunks(BukkitWorld dw, List<DynmapChunk> chunks) {
		this.w = dw.getWorld();
		super.setChunks(dw, chunks);
	}
}

