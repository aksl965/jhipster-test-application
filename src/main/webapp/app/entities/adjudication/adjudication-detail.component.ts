import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdjudication } from 'app/shared/model/adjudication.model';

@Component({
  selector: 'jhi-adjudication-detail',
  templateUrl: './adjudication-detail.component.html'
})
export class AdjudicationDetailComponent implements OnInit {
  adjudication: IAdjudication;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ adjudication }) => {
      this.adjudication = adjudication;
    });
  }

  previousState() {
    window.history.back();
  }
}
