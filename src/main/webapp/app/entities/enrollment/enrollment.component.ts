import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IEnrollment } from 'app/shared/model/enrollment.model';
import { AccountService } from 'app/core';
import { EnrollmentService } from './enrollment.service';

@Component({
  selector: 'jhi-enrollment',
  templateUrl: './enrollment.component.html'
})
export class EnrollmentComponent implements OnInit, OnDestroy {
  enrollments: IEnrollment[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected enrollmentService: EnrollmentService,
    protected jhiAlertService: JhiAlertService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.enrollmentService
      .query()
      .pipe(
        filter((res: HttpResponse<IEnrollment[]>) => res.ok),
        map((res: HttpResponse<IEnrollment[]>) => res.body)
      )
      .subscribe(
        (res: IEnrollment[]) => {
          this.enrollments = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInEnrollments();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IEnrollment) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInEnrollments() {
    this.eventSubscriber = this.eventManager.subscribe('enrollmentListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
