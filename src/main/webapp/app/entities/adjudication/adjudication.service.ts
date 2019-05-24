import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAdjudication } from 'app/shared/model/adjudication.model';

type EntityResponseType = HttpResponse<IAdjudication>;
type EntityArrayResponseType = HttpResponse<IAdjudication[]>;

@Injectable({ providedIn: 'root' })
export class AdjudicationService {
  public resourceUrl = SERVER_API_URL + 'api/adjudications';

  constructor(protected http: HttpClient) {}

  create(adjudication: IAdjudication): Observable<EntityResponseType> {
    return this.http.post<IAdjudication>(this.resourceUrl, adjudication, { observe: 'response' });
  }

  update(adjudication: IAdjudication): Observable<EntityResponseType> {
    return this.http.put<IAdjudication>(this.resourceUrl, adjudication, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdjudication>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdjudication[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
