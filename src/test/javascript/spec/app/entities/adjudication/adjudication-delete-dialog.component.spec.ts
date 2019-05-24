/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhipsterTestApplicationTestModule } from '../../../test.module';
import { AdjudicationDeleteDialogComponent } from 'app/entities/adjudication/adjudication-delete-dialog.component';
import { AdjudicationService } from 'app/entities/adjudication/adjudication.service';

describe('Component Tests', () => {
  describe('Adjudication Management Delete Component', () => {
    let comp: AdjudicationDeleteDialogComponent;
    let fixture: ComponentFixture<AdjudicationDeleteDialogComponent>;
    let service: AdjudicationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterTestApplicationTestModule],
        declarations: [AdjudicationDeleteDialogComponent]
      })
        .overrideTemplate(AdjudicationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdjudicationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AdjudicationService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
