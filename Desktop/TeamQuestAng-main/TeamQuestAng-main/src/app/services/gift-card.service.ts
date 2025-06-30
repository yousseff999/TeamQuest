import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GiftCard, ExchangeRequest } from '../models/gift-card';

@Injectable({
  providedIn: 'root'
})
export class GiftCardService {
  private apiUrl = 'http://192.168.56.10:8086/api/gift-cards';

  constructor(private http: HttpClient) {}

  getGiftCards(userId: number): Observable<GiftCard[]> {
    return this.http.get<GiftCard[]>(`${this.apiUrl}/${userId}`);
  }

  exchangeGiftCard(request: ExchangeRequest): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/exchange`, request);
  }
}
