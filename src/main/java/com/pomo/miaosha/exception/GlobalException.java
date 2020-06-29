package com.pomo.miaosha.exception;

import com.pomo.miaosha.result.CodeMsg;

public class GlobalException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private CodeMsg codeMsg;

	public GlobalException(CodeMsg codeMsg) {
		super(codeMsg.toString());
		this.codeMsg = codeMsg;
	}

	public CodeMsg getCm() {
		return codeMsg;
	}

	public void setCm(CodeMsg codeMsg) {
		this.codeMsg = codeMsg;
	}
}
