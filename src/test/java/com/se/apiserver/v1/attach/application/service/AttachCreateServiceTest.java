package com.se.apiserver.v1.attach.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.se.apiserver.v1.attach.application.dto.AttachReadDto;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.attach.infra.repository.AttachJpaRepository;
import com.se.apiserver.v1.multipartfile.application.dto.MultipartFileUploadDto;
import com.se.apiserver.v1.multipartfile.application.service.MultipartFileUploadService;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class AttachCreateServiceTest {

  private String data = "data";
  private MultipartFile[] files = {
      new MockMultipartFile("file"
          , "file.png"
          , "text/plain"
          , data.getBytes(StandardCharsets.UTF_8)),
      new MockMultipartFile("file"
          , "file.png"
          , "text/plain"
          , data.getBytes(StandardCharsets.UTF_8)),
      new MockMultipartFile("file"
          , "file.png"
          , "text/plain"
          , data.getBytes(StandardCharsets.UTF_8)),
      new MockMultipartFile("file"
          , "file.png"
          , "text/plain"
          , data.getBytes(StandardCharsets.UTF_8)),
      new MockMultipartFile("file"
          , "file.png"
          , "text/plain"
          , data.getBytes(StandardCharsets.UTF_8)),
  };

  @Mock
  private MultipartFileUploadService multipartFileUploadService;

  @Mock
  private AttachJpaRepository attachJpaRepository;

  @InjectMocks
  private AttachCreateService attachCreateService;

  @Test
  void 첨부_파일_등록_성공() {
    // given
    List<MultipartFileUploadDto> multipartFileUploadDtoList = Arrays.asList(
        new MultipartFileUploadDto("URL1", "FileName1", 1L),
        new MultipartFileUploadDto("URL2", "FileName2", 1L),
        new MultipartFileUploadDto("URL3", "FileName3", 1L),
        new MultipartFileUploadDto("URL4", "FileName4", 1L),
        new MultipartFileUploadDto("URL5", "FileName5", 1L)
    );

    given(multipartFileUploadService.upload(files)).willReturn(multipartFileUploadDtoList);
    when(attachJpaRepository.saveAll(any())).thenAnswer(new Answer<List<Attach>>() {
      @Override
      public List<Attach> answer(InvocationOnMock invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        return (List<Attach>) args[0];
      }
    });

    // when
    List<AttachReadDto.Response> responseList = attachCreateService.create(files);

    // then
    assertAll(
        () -> assertEquals(multipartFileUploadDtoList.get(0).getDownloadUrl(), responseList.get(0).getDownloadUrl()),
        () -> assertEquals(multipartFileUploadDtoList.get(1).getDownloadUrl(), responseList.get(1).getDownloadUrl()),
        () -> assertEquals(multipartFileUploadDtoList.get(2).getDownloadUrl(), responseList.get(2).getDownloadUrl()),
        () -> assertEquals(multipartFileUploadDtoList.get(3).getDownloadUrl(), responseList.get(3).getDownloadUrl()),
        () -> assertEquals(multipartFileUploadDtoList.get(4).getDownloadUrl(), responseList.get(4).getDownloadUrl()),
        () -> assertEquals(multipartFileUploadDtoList.get(0).getOriginalName(), responseList.get(0).getFileName()),
        () -> assertEquals(multipartFileUploadDtoList.get(1).getOriginalName(), responseList.get(1).getFileName()),
        () -> assertEquals(multipartFileUploadDtoList.get(2).getOriginalName(), responseList.get(2).getFileName()),
        () -> assertEquals(multipartFileUploadDtoList.get(3).getOriginalName(), responseList.get(3).getFileName()),
        () -> assertEquals(multipartFileUploadDtoList.get(4).getOriginalName(), responseList.get(4).getFileName()),
        () -> assertEquals(multipartFileUploadDtoList.get(0).getFileSize(), responseList.get(0).getFileSize()),
        () -> assertEquals(multipartFileUploadDtoList.get(1).getFileSize(), responseList.get(1).getFileSize()),
        () -> assertEquals(multipartFileUploadDtoList.get(2).getFileSize(), responseList.get(2).getFileSize()),
        () -> assertEquals(multipartFileUploadDtoList.get(3).getFileSize(), responseList.get(3).getFileSize()),
        () -> assertEquals(multipartFileUploadDtoList.get(4).getFileSize(), responseList.get(4).getFileSize())
    );
  }
}
