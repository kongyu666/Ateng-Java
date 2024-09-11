package local.kongyu.storage.config;

import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;

/**
 * 实现FileRecorder
 *
 * @author 孔余
 * @since 2024-02-18 09:52
 */
//@Component
public class MyFileRecorder implements FileRecorder {
    @Override
    public boolean save(FileInfo fileInfo) {
        return false;
    }

    @Override
    public void update(FileInfo fileInfo) {

    }

    @Override
    public FileInfo getByUrl(String s) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public void saveFilePart(FilePartInfo filePartInfo) {

    }

    @Override
    public void deleteFilePartByUploadId(String s) {

    }
}
