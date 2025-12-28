import request from '@/utils/request'

export const getPicUrl = () => {
  return request.get('/captcha/image')
}

export function getSmsCode (captchaCode, captchaKey, mobile) {
  return request.post('/captcha/sendSmsCaptcha', {
    captchaCode,
    captchaKey,
    mobile
  })
}

export function login (mobile, smsCode) {
  return request.post('/passport/login', {
    form: {
      isParty: false,
      mobile,
      partyData: {},
      smsCode
    }
  })
}
