import { Moment } from 'moment';
import { IPatient } from 'app/shared/model/patient.model';
import { IAdjudication } from 'app/shared/model/adjudication.model';

export interface IClaim {
  id?: number;
  claimNo?: number;
  claimType?: string;
  claimAmount?: number;
  claimDate?: Moment;
  content?: any;
  patient?: IPatient;
  adjudication?: IAdjudication;
}

export class Claim implements IClaim {
  constructor(
    public id?: number,
    public claimNo?: number,
    public claimType?: string,
    public claimAmount?: number,
    public claimDate?: Moment,
    public content?: any,
    public patient?: IPatient,
    public adjudication?: IAdjudication
  ) {}
}
