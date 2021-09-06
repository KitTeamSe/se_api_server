package com.se.apiserver.v1.attach.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
import org.mockito.junit.jupiter.MockitoExtension;
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
        new MultipartFileUploadDto("URL1", "FileName1"),
        new MultipartFileUploadDto("URL2", "FileName2"),
        new MultipartFileUploadDto("URL3", "FileName3"),
        new MultipartFileUploadDto("URL4", "FileName4"),
        new MultipartFileUploadDto("URL5", "FileName5")
    );
    List<Attach> attaches = Arrays.asList(
        new Attach("URL1", "FileName1"),
        new Attach("URL2", "FileName2"),
        new Attach("URL3", "FileName3"),
        new Attach("URL4", "FileName4"),
        new Attach("URL5", "FileName5")
    );

    given(multipartFileUploadService.upload(files)).willReturn(multipartFileUploadDtoList);
    given(attachJpaRepository.saveAll(any(List.class))).willReturn(attaches);

    // when
    List<AttachReadDto.Response> responseList = attachCreateService.create(files);

    // then
    assertThat(responseList.size(), is(multipartFileUploadDtoList.size()));
  }
}
