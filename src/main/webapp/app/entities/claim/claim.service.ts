import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClaim } from 'app/shared/model/claim.model';

type EntityResponseType = HttpResponse<IClaim>;
type EntityArrayResponseType = HttpResponse<IClaim[]>;

@Injectable({ providedIn: 'root' })
export class ClaimService {
  public resourceUrl = SERVER_API_URL + 'api/claims';

  constructor(protected http: HttpClient) {}

  create(claim: IClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(claim);
    return this.http
      .post<IClaim>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(claim: IClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(claim);
    return this.http
      .put<IClaim>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClaim>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClaim[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(claim: IClaim): IClaim {
    const copy: IClaim = Object.assign({}, claim, {
      claimDate: claim.claimDate != null && claim.claimDate.isValid() ? claim.claimDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.claimDate = res.body.claimDate != null ? moment(res.body.claimDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((claim: IClaim) => {
        claim.claimDate = claim.claimDate != null ? moment(claim.claimDate) : null;
      });
    }
    return res;
  }
}
