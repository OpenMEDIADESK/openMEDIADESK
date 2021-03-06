/*
 * This is public domain software - that is, you can do whatever you want
 * with it, and include it software that is licensed under the GNU or the
 * BSD license, or whatever other licence you choose, including proprietary
 * closed source licenses.  I do ask that you leave this header in tact.
 *
 * If you make modifications to this code that you think would benefit the
 * wider community, please send me a copy and I'll post it on my site.
 *
 * If you make use of this code, I'd appreciate hearing about it.
 *   drew@drewnoakes.com
 * Latest version of this software kept at
 *   http://drewnoakes.com/
 */
package com.drew.metadata.exif;

import com.drew.metadata.Directory;
import com.drew.metadata.MetadataException;
import com.drew.metadata.TagDescriptor;

/**
 * Provides human-readable string versions of the tags stored in a KodakMakernoteDirectory.
 * Thanks to David Carson for the initial version of this class.
 */
public class KodakMakernoteDescriptor extends TagDescriptor
{
	public KodakMakernoteDescriptor(Directory directory)
	{
		super(directory);
	}
	
	public String getDescription(int tagType) throws MetadataException
    {
		return _directory.getString(tagType);
	}
}
