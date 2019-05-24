import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IEnrollment, Enrollment } from 'app/shared/model/enrollment.model';
import { EnrollmentService } from './enrollment.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient';

@Component({
  selector: 'jhi-enrollment-update',
  templateUrl: './enrollment-update.component.html'
})
export class EnrollmentUpdateComponent implements OnInit {
  enrollment: IEnrollment;
  isSaving: boolean;

  patients: IPatient[];

  editForm = this.fb.group({
    id: [],
    planType: [],
    plantName: [],
    premiumValue: [],
    fromDate: [],
    toDate: [],
    content: [],
    patient: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected enrollmentService: EnrollmentService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ enrollment }) => {
      this.updateForm(enrollment);
      this.enrollment = enrollment;
    });
    this.patientService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPatient[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPatient[]>) => response.body)
      )
      .subscribe((res: IPatient[]) => (this.patients = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(enrollment: IEnrollment) {
    this.editForm.patchValue({
      id: enrollment.id,
      planType: enrollment.planType,
      plantName: enrollment.plantName,
      premiumValue: enrollment.premiumValue,
      fromDate: enrollment.fromDate != null ? enrollment.fromDate.format(DATE_TIME_FORMAT) : null,
      toDate: enrollment.toDate != null ? enrollment.toDate.format(DATE_TIME_FORMAT) : null,
      content: enrollment.content,
      patient: enrollment.patient
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
    const enrollment = this.createFromForm();
    if (enrollment.id !== undefined) {
      this.subscribeToSaveResponse(this.enrollmentService.update(enrollment));
    } else {
      this.subscribeToSaveResponse(this.enrollmentService.create(enrollment));
    }
  }

  private createFromForm(): IEnrollment {
    const entity = {
      ...new Enrollment(),
      id: this.editForm.get(['id']).value,
      planType: this.editForm.get(['planType']).value,
      plantName: this.editForm.get(['plantName']).value,
      premiumValue: this.editForm.get(['premiumValue']).value,
      fromDate: this.editForm.get(['fromDate']).value != null ? moment(this.editForm.get(['fromDate']).value, DATE_TIME_FORMAT) : undefined,
      toDate: this.editForm.get(['toDate']).value != null ? moment(this.editForm.get(['toDate']).value, DATE_TIME_FORMAT) : undefined,
      content: this.editForm.get(['content']).value,
      patient: this.editForm.get(['patient']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnrollment>>) {
    result.subscribe((res: HttpResponse<IEnrollment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
