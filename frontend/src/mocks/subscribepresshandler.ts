import { http, HttpResponse } from 'msw';

export const subscribepresshandlers = [
  // POST 요청을 가로채는 핸들러
  http.post(
    'https://j11a301.p.ssafy.io/api/v1/members/press/:pressId',
    (req) => {
      const { pressId } = req.params; // URL에서 pressId를 추출

      // 응답 생성
      return HttpResponse.json({
        message: `Press with ID ${pressId} subscribed successfully.`,
      });
    },
  ),
];