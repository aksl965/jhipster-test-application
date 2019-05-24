/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ClaimService } from 'app/entities/claim/claim.service';
import { IClaim, Claim } from 'app/shared/model/claim.model';

describe('Service Tests', () => {
  describe('Claim Service', () => {
    let injector: TestBed;
    let service: ClaimService;
    let httpMock: HttpTestingController;
    let elemDefault: IClaim;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(ClaimService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Claim(0, 0, 'AAAAAAA', 0, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            claimDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Claim', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            claimDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            claimDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Claim(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Claim', async () => {
        const returnedFromService = Object.assign(
          {
            claimNo: 1,
            claimType: 'BBBBBB',
            claimAmount: 1,
            claimDate: currentDate.format(DATE_TIME_FORMAT),
            content: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            claimDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Claim', async () => {
        const returnedFromService = Object.assign(
          {
            claimNo: 1,
            claimType: 'BBBBBB',
            claimAmount: 1,
            claimDate: currentDate.format(DATE_TIME_FORMAT),
            content: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            claimDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Claim', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
