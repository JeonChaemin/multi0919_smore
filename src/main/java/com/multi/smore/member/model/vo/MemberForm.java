package com.multi.smore.member.model.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberForm {
	@NotNull(message = "id가 입력되지 않았습니다.")
	@Size(max = 20, min = 4, message = "id를 4글자 이상, 20글자 미만 입력바랍니다.")
	private String memberId;
	
	@NotNull(message = "pw가 입력되지 않았습니다.")
	@Size(max = 20, min = 4, message = "패스워드를 4글자 이상, 20글자 미만 입력바랍니다.")
	private String password;

	@NotNull(message = "이름이 입력되지 않았습니다.")
	@Size(max = 20, min = 2, message = "이름을 2글자 이상, 20글자 미만 입력바랍니다.")
	private String name;
	private String phone;
	private String email;
	private String kakaoToken;
	private String address;
	
	public Member toMember(){
		return new Member(0, memberId, password, null, name, phone, email, address, kakaoToken, address, null, null);
	}
}
