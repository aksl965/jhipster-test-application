import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IClaim, Claim } from 'app/shared/model/claim.model';
import { ClaimService } from './claim.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient';
import { IAdjudication } from 'app/shared/model/adjudication.model';
import { AdjudicationService } from 'app/entities/adjudication';

@Component({
  selector: 'jhi-claim-update',
  templateUrl: './claim-update.component.html'
})
export class ClaimUpdateComponent implements OnInit {
  claim: IClaim;
  isSaving: boolean;

  patients: IPatient[];

  adjudications: IAdjudication[];

  editForm = this.fb.group({
    id: [],
    claimNo: [],
    claimType: [],
    claimAmount: [],
    claimDate: [],
    content: [],
    patient: [],
    adjudication: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected claimService: ClaimService,
    protected patientService: PatientService,
    protected adjudicationService: AdjudicationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ claim }) => {
      this.updateForm(claim);
      this.claim = claim;
    });
    this.patientService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPatient[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPatient[]>) => response.body)
      )
      .subscribe((res: IPatient[]) => (this.patients = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.adjudicationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IAdjudication[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAdjudication[]>) => response.body)
      )
      .subscribe((res: IAdjudication[]) => (this.adjudications = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(claim: IClaim) {
    this.editForm.patchValue({
      id: claim.id,
      claimNo: claim.claimNo,
      claimType: claim.claimType,
      claimAmount: claim.claimAmount,
      claimDate: claim.claimDate != null ? claim.claimDate.format(DATE_TIME_FORMAT) : null,
      content: claim.content,
      patient: claim.patient,
      adjudication: claim.adjudication
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const claim = this.createFromForm();
    if (claim.id !== undefined) {
      this.subscribeToSaveResponse(this.claimService.update(claim));
    } else {
      this.subscribeToSaveResponse(this.claimService.create(claim));
    }
  }

  private createFromForm(): IClaim {
    const entity = {
      ...new Claim(),
      id: this.editForm.get(['id']).value,
      claimNo: this.editForm.get(['claimNo']).value,
      claimType: this.editForm.get(['claimType']).value,
      claimAmount: this.editForm.get(['claimAmount']).value,
      claimDate:
        this.editForm.get(['claimDate']).value != null ? moment(this.editForm.get(['claimDate']).value, DATE_TIME_FORMAT) : undefined,
      content: this.editForm.get(['content']).value,
      patient: this.editForm.get(['patient']).value,
      adjudication: this.editForm.get(['adjudication']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClaim>>) {
    result.subscribe((res: HttpResponse<IClaim>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackPatientById(index: number, item: IPatient) {
    return item.id;
  }

  trackAdjudicationById(index: number, item: IAdjudication) {
    return item.id;
  }
}
