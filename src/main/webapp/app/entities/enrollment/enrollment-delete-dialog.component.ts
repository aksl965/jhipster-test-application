import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEnrollment } from 'app/shared/model/enrollment.model';
import { EnrollmentService } from './enrollment.service';

@Component({
  selector: 'jhi-enrollment-delete-dialog',
  templateUrl: './enrollment-delete-dialog.component.html'
})
export class EnrollmentDeleteDialogComponent {
  enrollment: IEnrollment;

  constructor(
    protected enrollmentService: EnrollmentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.enrollmentService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'enrollmentListModification',
        content: 'Deleted an enrollment'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-enrollment-delete-popup',
  template: ''
})
export class EnrollmentDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ enrollment }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(EnrollmentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.enrollment = enrollment;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/enrollment', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/enrollment', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
