package com.se.apiserver.v1.account.domain.entity;

import com.se.apiserver.v1.common.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Long accountId;

  @Size(min = 4, max = 20)
  @Column(nullable = false, unique = true)
  private String idString;

  @Column(nullable = false)
  private String password;

  @Size(min = 2, max = 20)
  @Column(nullable = false)
  private String name;

  @Size(min = 2, max = 20)
  @Column(nullable = false, unique = true)
  private String nickname;

  @Size(min = 8, max = 20)
  @Column(nullable = false, unique = true)
  private String studentId;

  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private AccountType type;

  @Size(min = 10, max = 20)
  @Column(length = 20, unique = true)
  private String phoneNumber;

  @Size(min = 4, max = 40)
  @Column(nullable = false, unique = true)
  @Email
  private String email;

  @Column(length = 20)
  @Size(min = 4, max = 20)
  private String lastSignInIp;

  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private InformationOpenAgree informationOpenAgree;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "question_id", referencedColumnName = "questionId", nullable = false)
  private Question question;

  @Column(length = 100, nullable = false)
  @Size(min = 2, max = 100)
  private String answer;

  @Builder
  public Account(Long accountId, @Size(min = 5, max = 20) String idString, String password,
      @Size(min = 2, max = 20) String name, @Size(min = 2, max = 20) String nickname,
      @Size(min = 8, max = 20) String studentId, AccountType type, @Size(min = 10, max = 20) String phoneNumber,
      @Size(min = 4, max = 40) String email, @Size(min = 4, max = 20) String lastSignInIp,
      InformationOpenAgree informationOpenAgree) {
    this.accountId = accountId;
    this.idString = idString;
    this.password = password;
    this.name = name;
    this.nickname = nickname;
    this.studentId = studentId;
    this.type = type;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.lastSignInIp = lastSignInIp;
    this.informationOpenAgree = informationOpenAgree;
  }

  public void updateIfNotNull(String name, AccountType type, InformationOpenAgree informationOpenAgree) {
    if(name != null)
      this.name = name;
    if(type != null)
      this.type = type;
    if(informationOpenAgree != null)
      this.informationOpenAgree = informationOpenAgree;
  }

  public void updateQnA(Question question, String answer) {
    this.question = question;
    this.answer = answer;
  }

  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }

  public void updateStudentId(String studentId) {
    this.studentId = studentId;
  }

  public void changePassword(String newPassword){
    this.password = newPassword;
  }


}
