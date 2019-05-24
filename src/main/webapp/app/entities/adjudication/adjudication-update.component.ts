import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IAdjudication, Adjudication } from 'app/shared/model/adjudication.model';
import { AdjudicationService } from './adjudication.service';

@Component({
  selector: 'jhi-adjudication-update',
  templateUrl: './adjudication-update.component.html'
})
export class AdjudicationUpdateComponent implements OnInit {
  adjudication: IAdjudication;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    claimnumber: [],
    casenumber: [],
    eob: []
  });

  constructor(protected adjudicationService: AdjudicationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ adjudication }) => {
      this.updateForm(adjudication);
      this.adjudication = adjudication;
    });
  }

  updateForm(adjudication: IAdjudication) {
    this.editForm.patchValue({
      id: adjudication.id,
      claimnumber: adjudication.claimnumber,
      casenumber: adjudication.casenumber,
      eob: adjudication.eob
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const adjudication = this.createFromForm();
    if (adjudication.id !== undefined) {
      this.subscribeToSaveResponse(this.adjudicationService.update(adjudication));
    } else {
      this.subscribeToSaveResponse(this.adjudicationService.create(adjudication));
    }
  }

  private createFromForm(): IAdjudication {
    const entity = {
      ...new Adjudication(),
      id: this.editForm.get(['id']).value,
      claimnumber: this.editForm.get(['claimnumber']).value,
      casenumber: this.editForm.get(['casenumber']).value,
      eob: this.editForm.get(['eob']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdjudication>>) {
    result.subscribe((res: HttpResponse<IAdjudication>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
