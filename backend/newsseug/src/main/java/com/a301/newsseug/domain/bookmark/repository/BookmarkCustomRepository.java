package com.a301.newsseug.domain.bookmark.repository;

import com.a301.newsseug.domain.bookmark.model.entity.Bookmark;
import com.a301.newsseug.domain.folder.model.entity.Folder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkCustomRepository {

    Slice<Bookmark> findAllByFolder(Folder folder, Pageable pageable);

}
