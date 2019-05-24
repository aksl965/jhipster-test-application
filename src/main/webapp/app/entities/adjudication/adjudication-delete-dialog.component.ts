import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAdjudication } from 'app/shared/model/adjudication.model';
import { AdjudicationService } from './adjudication.service';

@Component({
  selector: 'jhi-adjudication-delete-dialog',
  templateUrl: './adjudication-delete-dialog.component.html'
})
export class AdjudicationDeleteDialogComponent {
  adjudication: IAdjudication;

  constructor(
    protected adjudicationService: AdjudicationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.adjudicationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'adjudicationListModification',
        content: 'Deleted an adjudication'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-adjudication-delete-popup',
  template: ''
})
export class AdjudicationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ adjudication }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(AdjudicationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.adjudication = adjudication;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/adjudication', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/adjudication', { outlets: { popup: null } }]);
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
