package com.imagepicker.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import com.facebook.react.bridge.ReadableMap;

import java.io.File;

/**
 * Created by rusfearuth on 15.03.17.
 */

public class ImageConfig
{
    public @Nullable final File original;
    public @Nullable final File resized;
    public final int maxWidth;
    public final int maxHeight;
    public final int quality;
    public final int rotation;
    public final boolean saveToCameraRoll;
    public final String extension;

    public ImageConfig(@Nullable final File original,
                       @Nullable final File resized,
                       final int maxWidth,
                       final int maxHeight,
                       final int quality,
                       final int rotation,
                       final boolean saveToCameraRoll,
                       final String extension)
    {
        this.original = original;
        this.resized = resized;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.quality = quality;
        this.rotation = rotation;
        this.saveToCameraRoll = saveToCameraRoll;
        this.extension = extension;
    }

    public @NonNull ImageConfig withMaxWidth(final int maxWidth)
    {
        return new ImageConfig(
                this.original, this.resized, maxWidth,
                this.maxHeight, this.quality, this.rotation,
                this.saveToCameraRoll, this.extension
        );
    }

    public @NonNull ImageConfig withMaxHeight(final int maxHeight)
    {
        return new ImageConfig(
                this.original, this.resized, this.maxWidth,
                maxHeight, this.quality, this.rotation,
                this.saveToCameraRoll, this.extension
        );

    }

    public @NonNull ImageConfig withQuality(final int quality)
    {
        return new ImageConfig(
                this.original, this.resized, this.maxWidth,
                this.maxHeight, quality, this.rotation,
                this.saveToCameraRoll, this.extension
        );
    }

    public @NonNull ImageConfig withRotation(final int rotation)
    {
        return new ImageConfig(
                this.original, this.resized, this.maxWidth,
                this.maxHeight, this.quality, rotation,
                this.saveToCameraRoll, this.extension
        );
    }

    public @NonNull ImageConfig withOriginalFile(@Nullable final File original)
    {
        if (original != null) {
            //if it is a GIF file, always set quality to 100 to prevent compression
            String extension = MimeTypeMap.getFileExtensionFromUrl(original.getAbsolutePath());
            int quality = this.quality;
            String Extension = this.extension;
            if (extension.contains("gif")) {
                quality = 100;
                Extension = "gif";
            } else if (extension.contains("png")) {
                Extension = "png";
            } else if (extension.contains("jpeg")) {
                Extension = "jpeg";
            }
        }

        return new ImageConfig(
                original, this.resized, this.maxWidth,
                this.maxHeight, quality, this.rotation,
                this.saveToCameraRoll, extension
        );
    }

    public @NonNull ImageConfig withResizedFile(@Nullable final File resized)
    {
        return new ImageConfig(
                this.original, resized, this.maxWidth,
                this.maxHeight, this.quality, this.rotation,
                this.saveToCameraRoll, this.extension
        );
    }

    public @NonNull ImageConfig withSaveToCameraRoll(@Nullable final boolean saveToCameraRoll)
    {
        return new ImageConfig(
                this.original, this.resized, this.maxWidth,
                this.maxHeight, this.quality, this.rotation,
                saveToCameraRoll, this.extension
        );
    }

    public @NonNull ImageConfig updateFromOptions(@NonNull final ReadableMap options)
    {
        int maxWidth = 0;
        if (options.hasKey("maxWidth"))
        {
            maxWidth = (int) options.getDouble("maxWidth");
        }
        int maxHeight = 0;
        if (options.hasKey("maxHeight"))
        {
            maxHeight = (int) options.getDouble("maxHeight");
        }
        int quality = 100;
        if (options.hasKey("quality"))
        {
            quality = (int) (options.getDouble("quality") * 100);
        }
        int rotation = 0;
        if (options.hasKey("rotation"))
        {
            rotation = (int) options.getDouble("rotation");
        }
        boolean saveToCameraRoll = false;
        if (options.hasKey("storageOptions"))
        {
            final ReadableMap storageOptions = options.getMap("storageOptions");
            if (storageOptions.hasKey("cameraRoll"))
            {
                saveToCameraRoll = storageOptions.getBoolean("cameraRoll");
            }
        }
        String extension = "jpg";   // by default
        if (options.hasKey("extension")) {
            extension = options.getString("extension");
        }
        return new ImageConfig(this.original, this.resized, maxWidth, maxHeight, quality, rotation, saveToCameraRoll, extension);
    }

    public boolean useOriginal(int initialWidth,
                               int initialHeight,
                               int currentRotation)
    {
        return ((initialWidth < maxWidth && maxWidth > 0) || maxWidth == 0) &&
                ((initialHeight < maxHeight && maxHeight > 0) || maxHeight == 0) &&
                quality == 100 && (rotation == 0 || currentRotation == rotation);
    }

    public File getActualFile()
    {
        return resized != null ? resized: original;
    }
}
