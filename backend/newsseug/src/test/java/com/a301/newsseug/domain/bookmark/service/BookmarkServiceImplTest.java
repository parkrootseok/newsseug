package com.a301.newsseug.domain.bookmark.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("북마크 관련 기능")
@ExtendWith(MockitoExtension.class)
class BookmarkServiceImplTest {

//    @Mock
//    private FolderRepository folderRepository;
//
//    @Mock
//    private ArticleRepository articleRepository;
//
//    @Mock
//    private BookmarkRepository bookmarkRepository;
//
//    @Mock
//    private CustomUserDetails userDetails;
//
//    @InjectMocks
//    private BookmarkServiceImpl bookmarkService;
//
//    private Member loginMember;
//
//    @BeforeEach
//    void beforeEach() {
//        loginMember = MemberFactory.memberOfKakao(1L);
//        given(userDetails.getMember()).willReturn(loginMember);
//    }
//
//    @Test
//    @DisplayName("북마크 생성[성공]")
//    void createBookmark() {
//
//        // Given
//        Long articleId = 1L;
//        List<Long> folderIds = List.of(1L, 2L);
//        CreateBookmarkRequest request = BookmarkRequestFactory.createBookmarkRequest(articleId, folderIds);
//        List<Folder> folders = request.folderIds()
//                .stream()
//                .map(FolderFactory::folder)
//                .toList();
//
//        given(articleRepository.findOrThrow(request.articleId())).willReturn(ArticleFactory.article(articleId));
//        given(folderRepository.findByFolderIdAndMemberAndActivationStatus(1L, loginMember, ActivationStatus.ACTIVE))
//                .willReturn(Optional.of(folders.get(0)));
//        given(folderRepository.findByFolderIdAndMemberAndActivationStatus(2L, loginMember, ActivationStatus.ACTIVE))
//                .willReturn(Optional.of(folders.get(1)));
//
//        // When
//        bookmarkService.createBookmark(userDetails, request);
//
//        // Then
//        verify(bookmarkRepository).saveAll(anyList());
//
//    }
//
//    @Test
//    @DisplayName("북마크 생성[실패 - 접근 불가 폴더]")
//    void createBookmarkInaccessibleFolder() {
//
//        // Given
//        Long articleId = 1L;
//        List<Long> folderIds = List.of(1L, 2L);
//        CreateBookmarkRequest request = BookmarkRequestFactory.createBookmarkRequest(articleId, folderIds);
//
//        given(articleRepository.findOrThrow(request.articleId())).willReturn(ArticleFactory.article(articleId));
//        given(folderRepository.findByFolderIdAndMemberAndActivationStatus(1L, loginMember, ActivationStatus.ACTIVE))
//                .willReturn(Optional.empty());
//
//        // Then
//        assertThatThrownBy(() -> bookmarkService.createBookmark(userDetails, request))
//                .isInstanceOf(InaccessibleFolderException.class);
//
//    }
//
//    @Test
//    @DisplayName("썸네일 업데이트[성공]")
//    void updateFolderThumbnail() {
//
//        // Given
//        Article article = ArticleFactory.article(1L);
//        List<Long> folderIds = List.of(1L);
//        Folder folder = FolderFactory.folder(1L);
//
//        given(articleRepository.findOrThrow(article.getId())).willReturn(article);
//        given(folderRepository.findByFolderIdAndMemberAndActivationStatus(folder.getId(), loginMember, ActivationStatus.ACTIVE))
//                .willReturn(Optional.of(folder));
//
//        // When
//        bookmarkService
//                .createBookmark(userDetails, BookmarkRequestFactory.createBookmarkRequest(article.getId(), folderIds));
//
//        // Then
//        assertThat(folder.getThumbnailUrl()).isEqualTo(article.getThumbnailUrl());
//
//    }
//

}
